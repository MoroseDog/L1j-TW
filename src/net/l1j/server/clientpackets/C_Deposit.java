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
package net.l1j.server.clientpackets;

import net.l1j.server.ClientThread;
import net.l1j.server.datatables.CastleTable;
import net.l1j.server.model.L1Clan;
import net.l1j.server.model.L1World;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.item.ItemId;
import net.l1j.server.templates.L1Castle;

public class C_Deposit extends ClientBasePacket {
	private static final String C_DEPOSIT = "[C] C_Deposit";

	public C_Deposit(byte abyte0[], ClientThread clientthread) throws Exception {
		super(abyte0);

		int i = readD();
		int j = readD();

		L1PcInstance player = clientthread.getActiveChar();
		if (i == player.getId()) {
			L1Clan clan = L1World.getInstance().getClan(player.getClanname());
			if (clan != null) {
				int castle_id = clan.getCastleId();
				if (castle_id != 0) { // 城主クラン
					L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
					synchronized (l1castle) {
						int money = l1castle.getPublicMoney();
						if (player.getInventory().consumeItem(ItemId.ADENA, j)) {
							money += j;
							l1castle.setPublicMoney(money);
							CastleTable.getInstance().updateCastle(l1castle);
						}
					}
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_DEPOSIT;
	}
}
