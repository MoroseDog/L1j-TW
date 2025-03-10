/*
isExsistMaster * This program is free software; you can redistribute it and/or modify
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
package net.l1j.server.serverpackets;

import net.l1j.server.Opcodes;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1SummonInstance;

public class S_SummonPack extends ServerBasePacket {

	private static final int STATUS_POISON = 1;
	private static final int STATUS_INVISIBLE = 2;
	private static final int STATUS_PC = 4;
	private static final int STATUS_FREEZE = 8;
	private static final int STATUS_BRAVE = 16;
	private static final int STATUS_ELFBRAVE = 32;
	private static final int STATUS_FASTMOVABLE = 64;
	private static final int STATUS_GHOST = 128;

	private byte[] _byte = null;

	public S_SummonPack(L1SummonInstance pet, L1PcInstance pc) {
		buildPacket(pet, pc, true);
	}

	public S_SummonPack(L1SummonInstance pet, L1PcInstance pc, boolean isCheckMaster) {
		buildPacket(pet, pc, isCheckMaster);
	}

	private void buildPacket(L1SummonInstance pet, L1PcInstance pc, boolean isCheckMaster) {
		writeC(Opcodes.S_OPCODE_CHARPACK);
		writeH(pet.getX());
		writeH(pet.getY());
		writeD(pet.getId());
		writeH(pet.getGfxId()); // SpriteID in List.spr
		writeC(pet.getStatus()); // Modes in List.spr
		writeC(pet.getHeading());
		writeC(pet.getChaLightSize()); // (Bright) - 0~15
		writeC(pet.getMoveSpeed()); // スピード - 0:normal, 1:fast, 2:slow
		writeD(0);
		writeH(0);
		writeS(pet.getNameId());
		writeS(pet.getTitle());
		int status = 0;
		if (pet.getPoison() != null) { // 毒狀態
			if (pet.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
			}
		}
		writeC(status);
		writeD(0);
		writeS(null);
		if (isCheckMaster && pet.isExsistMaster()) {
			writeS(pet.getMaster().getName());
		} else {
			writeS("");
		}
		writeC(0); // ??
		// HPのパーセント
		if (pet.getMaster() != null && pet.getMaster().getId() == pc.getId()) {
			int percent = pet.getMaxHp() != 0 ? 100 * pet.getCurrentHp() / pet.getMaxHp() : 100;
			writeC(percent);
		} else {
			writeC(0xFF);
		}
		writeC(0);
		writeC(pet.getLevel()); // PC = 0, Mon = Lv
		writeC(0);
		writeC(0xFF);
		writeC(0xFF);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}
}
