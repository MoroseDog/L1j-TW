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
package net.l1j.server.command.executor;

import net.l1j.server.model.L1Object;
import net.l1j.server.model.instance.L1MonsterInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.model.instance.L1PetInstance;
import net.l1j.server.model.instance.L1SummonInstance;
import net.l1j.server.serverpackets.S_HPMeter;
import net.l1j.server.serverpackets.S_SystemMessage;

import static net.l1j.server.model.skill.SkillId.*;

public class L1HpBar implements L1CommandExecutor {
	public static L1CommandExecutor getInstance() {
		return new L1HpBar();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		if (arg.equalsIgnoreCase("on")) {
			pc.setSkillEffect(GMSTATUS_HPBAR, 0);
		} else if (arg.equalsIgnoreCase("off")) {
			pc.removeSkillEffect(GMSTATUS_HPBAR);

			for (L1Object obj : pc.getKnownObjects()) {
				if (isHpBarTarget(obj)) {
					pc.sendPackets(new S_HPMeter(obj.getId(), 0xFF));
				}
			}
		} else {
			pc.sendPackets(new S_SystemMessage("請輸入 " + cmdName + " on|off。"));
		}
	}

	public static boolean isHpBarTarget(L1Object obj) {
		if (obj instanceof L1MonsterInstance) {
			return true;
		}
		if (obj instanceof L1PcInstance) {
			return true;
		}
		if (obj instanceof L1SummonInstance) {
			return true;
		}
		if (obj instanceof L1PetInstance) {
			return true;
		}
		return false;
	}
}
