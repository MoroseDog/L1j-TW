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
package net.l1j.util;

import javolution.util.FastSet;

import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1Location;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1DollInstance;
import net.l1j.server.model.instance.L1NpcInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.model.map.L1Map;
import net.l1j.server.model.map.L1WorldMap;
import net.l1j.server.serverpackets.S_CharVisualUpdate;
import net.l1j.server.serverpackets.S_DollPack;
import net.l1j.server.serverpackets.S_SkillIconWindShackle;
import net.l1j.server.serverpackets.S_MapID;
import net.l1j.server.serverpackets.S_OtherCharPacks;
import net.l1j.server.serverpackets.S_OwnCharPack;
import net.l1j.server.serverpackets.S_PetPack;
import net.l1j.server.serverpackets.S_SummonPack;

import static net.l1j.server.model.skill.SkillId.*;

public class Teleportation {
	public static void Teleportation(L1PcInstance pc) {
		if (pc.isDead() || pc.isTeleport()) {
			return;
		}

		int x = pc.getTeleportX();
		int y = pc.getTeleportY();
		short mapId = pc.getTeleportMapId();
		int head = pc.getTeleportHeading();

		// テレポート先が不正であれば元の座標へ(GMは除く)
		L1Map map = L1WorldMap.getInstance().getMap(mapId);

		if (!map.isInMap(x, y) && !pc.isGm()) {
			x = pc.getX();
			y = pc.getY();
			mapId = pc.getMapId();
		}

		pc.setTeleport(true);

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			if (clan.getWarehouseUsingChar() == pc.getId()) { // 自キャラがクラン倉庫使用中
				clan.setWarehouseUsingChar(0); // クラン倉庫のロックを解除
			}
		}

		L1World.getInstance().moveVisibleObject(pc, mapId);
		pc.setLocation(x, y, mapId);
		pc.setHeading(head);
		pc.sendPackets(new S_MapID(pc.getMapId(), pc.getMap().isUnderwater()));

		if (pc.isReserveGhost()) { // ゴースト狀態解除
			pc.endGhost();
		}
		if (pc.isGhost() || pc.isGmInvis()) {
		} else if (pc.isInvisble()) {
			pc.broadcastPacketForFindInvis(new S_OtherCharPacks(pc, true), true);
		} else {
			pc.broadcastPacket(new S_OtherCharPacks(pc));
		}
		pc.sendPackets(new S_OwnCharPack(pc));

		pc.removeAllKnownObjects();
		pc.sendVisualEffectAtTeleport(); // クラウン、毒、水中等の視覺效果を表示
		pc.updateObject();
		// spr番號6310, 5641の變身中にテレポートするとテレポート後に移動できなくなる
		// 武器を著脫すると移動できるようになるため、S_CharVisualUpdateを送信する
		pc.sendPackets(new S_CharVisualUpdate(pc));

		pc.killSkillEffectTimer(SKILL_MEDITATION);
		pc.setCallClanId(0); // コールクランを唱えた後に移動すると召喚無效

		/*
		 * subjects ペットとサモンのテレポート先畫面內へ居たプレイヤー。
		 * 各ペット每にUpdateObjectを行う方がコード上ではスマートだが、
		 * ネットワーク負荷が大きくなる為、一旦Setへ格納して最後にまとめてUpdateObjectする。
		 */
		FastSet<L1PcInstance> subjects = new FastSet<L1PcInstance>();
		subjects.add(pc);
/*
		if (!pc.isGhost()) {
			if (pc.getMap().isTakePets()) {
				// ペットとサモンも一緒に移動させる。
				for (L1NpcInstance petNpc : pc.getPetList().values()) {

					// テレポート先の設定
					L1Location loc = pc.getLocation().randomLocation(3, false);
					int nx = loc.getX();
					int ny = loc.getY();
					if (pc.getMapId() == 5125 || pc.getMapId() == 5131
							|| pc.getMapId() == 5132 || pc.getMapId() == 5133
							|| pc.getMapId() == 5134) { // ペットマッチ會場
						nx = RandomArrayList.getInc(7, 32799 - 3);
						ny = RandomArrayList.getInc(7, 32864 - 3);
					}
					teleport(petNpc, nx, ny, mapId, head);
					if (petNpc instanceof L1SummonInstance) { // サモンモンスター
						L1SummonInstance summon = (L1SummonInstance) petNpc;
						pc.sendPackets(new S_SummonPack(summon, pc));
					} else if (petNpc instanceof L1PetInstance) { // ペット
						L1PetInstance pet = (L1PetInstance) petNpc;
						pc.sendPackets(new S_PetPack(pet, pc));
					}

					for (L1PcInstance visiblePc : L1World.getInstance()
							.getVisiblePlayer(petNpc)) {
						// テレポート元と先に同じPCが居た場合、正しく更新されない為、一度removeする。
						visiblePc.removeKnownObject(petNpc);
						subjects.add(visiblePc);
					}

				}

				// マジックドールも一緒に移動させる。
				for (L1DollInstance doll : pc.getDollList().values()) {

					// テレポート先の設定
					L1Location loc = pc.getLocation().randomLocation(3, false);
					int nx = loc.getX();
					int ny = loc.getY();

					teleport(doll, nx, ny, mapId, head);
					pc.sendPackets(new S_DollPack(doll, pc));

					for (L1PcInstance visiblePc : L1World.getInstance()
							.getVisiblePlayer(doll)) {
						// テレポート元と先に同じPCが居た場合、正しく更新されない為、一度removeする。
						visiblePc.removeKnownObject(doll);
						subjects.add(visiblePc);
					}

				}
			}
		} else {
			for (L1DollInstance doll : pc.getDollList().values()) {

				// テレポート先の設定
				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();

				teleport(doll, nx, ny, mapId, head);
				pc.sendPackets(new S_DollPack(doll, pc));

				for (L1PcInstance visiblePc : L1World.getInstance()
						.getVisiblePlayer(doll)) {
					// テレポート元と先に同じPCが居た場合、正しく更新されない為、一度removeする。
					visiblePc.removeKnownObject(doll);
					subjects.add(visiblePc);
				}

			}
		}

		for (L1PcInstance updatePc : subjects) {
			updatePc.updateObject();
		}

		pc.setTeleport(false);
*/
		if (!pc.isGhost()) {
			if (pc.getMap().isTakePets()) {
				// ペットとサモンも一緒に移動させる。
				for (L1NpcInstance petNpc : pc.getPetList().values()) {

					// テレポート先の設定
					L1Location loc = pc.getLocation().randomLocation(3, false);
					int nx = loc.getX();
					int ny = loc.getY();
					if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132
							|| pc.getMapId() == 5133 || pc.getMapId() == 5134) { // ペットマッチ会場
						nx = RandomArrayList.getInc(7, 32799 - 3); // 32799 + StaticFinalList.getRang3();
						ny = RandomArrayList.getInc(7, 32864 - 3); // 32864 + StaticFinalList.getRang3();
					}
					teleport(petNpc, nx, ny, mapId, head);
					if (petNpc instanceof L1SummonInstance) { // サモンモンスター
						L1SummonInstance summon = (L1SummonInstance) petNpc;
						pc.sendPackets(new S_SummonPack(summon, pc));
					} else if (petNpc instanceof L1PetInstance) { // ペット
						L1PetInstance pet = (L1PetInstance) petNpc;
						pc.sendPackets(new S_PetPack(pet, pc));
					}

					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(petNpc)) {
						// テレポート元と先に同じPCが居た場合、正しく更新されない為、一度removeする。
						visiblePc.removeKnownObject(petNpc);
						subjects.add(visiblePc);
					}
				}
			} else {
				for (L1DollInstance doll : pc.getDollList().values()) {
					// テレポート先の設定
					L1Location loc = pc.getLocation().randomLocation(3, false);
					int nx = loc.getX();
					int ny = loc.getY();
					teleport(doll, nx, ny, mapId, head);
					pc.sendPackets(new S_DollPack(doll, pc));
					for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(doll)) {
						// テレポート元と先に同じPCが居た場合、正しく更新されない為、一度removeする。
						visiblePc.removeKnownObject(doll);
						subjects.add(visiblePc);
					}
				}
			}

			// マジックドールも一緒に移動させる。
			for (L1DollInstance doll : pc.getDollList().values()) {

				// テレポート先の設定
				L1Location loc = pc.getLocation().randomLocation(3, false);
				int nx = loc.getX();
				int ny = loc.getY();

				teleport(doll, nx, ny, mapId, head);
				pc.sendPackets(new S_DollPack(doll, pc));

				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(doll)) {
					// テレポート元と先に同じPCが居た場合、正しく更新されない為、一度removeする。
					visiblePc.removeKnownObject(doll);
					subjects.add(visiblePc);
				}

			}
		}

		for (L1PcInstance updatePc : subjects) {
			updatePc.updateObject();
		}

		pc.setTeleport(false);

		if (pc.hasSkillEffect(SKILL_WIND_SHACKLE)) {
			pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), pc.getSkillEffectTimeSec(SKILL_WIND_SHACKLE)));
		}
	}

	private static void teleport(L1NpcInstance npc, int x, int y, short map, int head) {
		L1World.getInstance().moveVisibleObject(npc, map);

		L1WorldMap.getInstance().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), true);
		npc.setX(x);
		npc.setY(y);
		npc.setMap(map);
		npc.setHeading(head);
		L1WorldMap.getInstance().getMap(npc.getMapId()).setPassable(npc.getX(), npc.getY(), false);
	}
}
