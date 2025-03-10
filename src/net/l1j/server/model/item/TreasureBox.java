/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.l1j.server.model.item;

import java.io.File;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javolution.util.FastMap;

import net.l1j.server.datatables.ItemTable;
import net.l1j.server.model.L1Inventory;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1ItemInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.util.PerformanceTimer;
import net.l1j.util.RandomArrayList;

@XmlAccessorType(XmlAccessType.FIELD)
public class TreasureBox {
	private final static Logger _log = Logger.getLogger(TreasureBox.class.getName());

	private static final FastMap<Integer, TreasureBox> _dataMap = new FastMap<Integer, TreasureBox>();

	private static final String PATH = "./data/xml/Item/TreasureBox.xml";

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "TreasureBoxList")
	private static class TreasureBoxList implements Iterable<TreasureBox> {
		@XmlElement(name = "TreasureBox")
		private List<TreasureBox> _list;

		public Iterator<TreasureBox> iterator() {
			return _list.iterator();
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Item {
		@XmlAttribute(name = "ItemId")
		private int _itemId;

		@XmlAttribute(name = "Count")
		private int _count;

		private int _chance;

		@XmlAttribute(name = "Chance")
		private void setChance(double chance) {
			_chance = (int) (chance * 10000);
		}

		public int getItemId() {
			return _itemId;
		}

		public int getCount() {
			return _count;
		}

		public double getChance() {
			return _chance;
		}
	}

	private static enum TYPE {
		RANDOM, SPECIFIC
	}

	/**
	 * 指定されたIDのTreasureBoxを返す。
	 * 
	 * @param id - TreasureBoxのID。普通はアイテムのItemIdになる。
	 * @return 指定されたIDのTreasureBox。見つからなかった場合はnull。
	 */
	public static TreasureBox get(int id) {
		return _dataMap.get(id);
	}

	@XmlAttribute(name = "ItemId")
	private int _boxId;

	@XmlAttribute(name = "Type")
	private TYPE _type;

	private int getBoxId() {
		return _boxId;
	}

	private TYPE getType() {
		return _type;
	}

	@XmlElement(name = "Item")
	private CopyOnWriteArrayList<Item> _items;

	private List<Item> getItems() {
		return _items;
	}

	private int _totalChance;

	private int getTotalChance() {
		return _totalChance;
	}

	private void init() {
		for (Item each : getItems()) {
			_totalChance += each.getChance();
			if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
				getItems().remove(each);
				_log.warning("對象 ID ：" + each.getItemId() + " 無法找到對應的Template。");
			}
		}
		if (getTotalChance() != 0 && getTotalChance() != 1000000) {
			_log.warning("ID ：" + getBoxId() + " 的機率總合不等於100%。");
		}
	}

	public static void load() {
		PerformanceTimer timer = new PerformanceTimer();
		System.out.print("╚》正在讀取 TreasureBox...");
		try {
			JAXBContext context = JAXBContext.newInstance(TreasureBox.TreasureBoxList.class);

			Unmarshaller um = context.createUnmarshaller();

			File file = new File(PATH);
			TreasureBoxList list = (TreasureBoxList) um.unmarshal(file);

			for (TreasureBox each : list) {
				each.init();
				_dataMap.put(each.getBoxId(), each);
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, PATH + "的載入失敗。", e);
			System.exit(0);
		}
		System.out.println("完成!\t耗時: " + timer.get() + "ms");
	}

	/**
	 * TreasureBoxを開けるPCにアイテムを入手させる。PCがアイテムを持ちきれなかった場合は アイテムは地面に落ちる。
	 * 
	 * @param pc - TreasureBoxを開けるPC
	 * @return 開封した結果何らかのアイテムが出てきた場合はtrueを返す。 持ちきれず地面に落ちた場合もtrueになる。
	 */
	public boolean open(L1PcInstance pc) {
		L1ItemInstance item = null;

		// 容量確認
		if (pc.getInventory().getSize() >= 175) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$263)); // 上述超過175或等於175樣時禁止開啟
			return false;
		}
		// 重量確認
		if ((pc.getInventory().getWeight() / pc.getMaxWeight() * 100) > 90) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$82)); // 負重大於90%時禁止開啟
			return false;
		}

		if (getType().equals(TYPE.SPECIFIC)) {
			// 出るアイテムが決まっているもの
			for (Item each : getItems()) {
				item = ItemTable.getInstance().createItem(each.getItemId());
				if (item != null) {
					item.setCount(each.getCount());
					storeItem(pc, item);
				}
			}
		} else if (getType().equals(TYPE.RANDOM)) {
			// 出るアイテムがランダムに決まるもの
			int chance = 0;

			int r = RandomArrayList.getInt(getTotalChance());

			for (Item each : getItems()) {
				chance += each.getChance();

				if (r < chance) {
					item = ItemTable.getInstance().createItem(each.getItemId());
					if (item != null) {
						item.setCount(each.getCount());
						storeItem(pc, item);
					}
					break;
				}
			}
		}

		if (item == null) {
			return false;
		} else {
			int itemId = getBoxId();

			// 魂の結晶の破片、魔族のスクロール、ブラックエントの實
			if (itemId == 40576 || itemId == 40577 || itemId == 40578 || itemId == 40411 || itemId == 49013) {
				pc.death(null); // キャラクターを死亡させる
			}
			return true;
		}
	}

	private static void storeItem(L1PcInstance pc, L1ItemInstance item) {
		L1Inventory inventory;

//		if (pc.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
			inventory = pc.getInventory();
//		} else {
			// 持てない場合は地面に落とす 處理のキャンセルはしない（不正防止）
//			inventory = L1World.getInstance().getInventory(pc.getLocation());
//		}
		inventory.storeItem(item);
		pc.sendPackets(new S_ServerMessage(SystemMessageId.$403, item.getLogName()));
	}
}
