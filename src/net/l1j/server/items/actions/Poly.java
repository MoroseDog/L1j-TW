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
package net.l1j.server.items.actions;

import net.l1j.server.datatables.PolyTable;
import net.l1j.server.datatables.SkillsTable;
import net.l1j.server.model.L1Character;
import net.l1j.server.model.L1PolyMorph;
import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.model.instance.L1MonsterInstance;
import net.l1j.server.model.instance.L1PcInstance;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.server.serverpackets.S_ShowPolyList;
import net.l1j.server.templates.L1Skills;
import net.l1j.server.utils.RandomArrayList;

import static net.l1j.server.skills.SkillId.SKILL_AWAKEN_ANTHARAS;
import static net.l1j.server.skills.SkillId.SKILL_AWAKEN_FAFURION;
import static net.l1j.server.skills.SkillId.SKILL_AWAKEN_VALAKAS;
import static net.l1j.server.skills.SkillId.SKILL_POLYMORPH;

public class Poly {

	/** 變身作用動作 */
	public static void Action(L1PcInstance attacker, L1Character cha) {
		boolean isSameClan = false;
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getClanid() != 0 && attacker.getClanid() == pc.getClanid()) {
				isSameClan = true;
			}
		}
		if (attacker.getId() != cha.getId() && !isSameClan) { // 自分以外と違うクラン
			int probability = 3 * (attacker.getLevel() - cha.getLevel()) + 100 - cha.getMr();
			//int rnd = RandomArrayList.getArray100List();
			if (RandomArrayList.getInc(100, 1) > probability) {
				return;
			}
		}

		int[] polyArray = { 29, 945, 947, 979, 1037, 1039, 3860, 3861, 3862, 3863, 3864, 3865,
				3904, 3906, 95, 146, 2374, 2376, 2377, 2378, 3866, 3867, 3868, 3869, 3870, 3871,
				3872, 3873, 3874, 3875, 3876 };

		int pid = RandomArrayList.getInt(polyArray.length);
		int polyId = polyArray[pid];

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int awakeSkillId = pc.getAwakeSkillId();
			if (awakeSkillId == SKILL_AWAKEN_ANTHARAS || awakeSkillId == SKILL_AWAKEN_FAFURION
					|| awakeSkillId == SKILL_AWAKEN_VALAKAS) {
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$1384));
				return;
			}

			if (pc.getInventory().checkEquipped(20281)) {
				pc.sendPackets(new S_ShowPolyList(pc.getId()));
				if (!pc.isShapeChange()) {//變身判斷
					pc.setShapeChange(true);
				}
				pc.sendPackets(new S_ServerMessage(SystemMessageId.$966));
				// 魔法の力によって保護されます。
				// 變身の際のメッセージは、他人が自分を變身させた時に出るメッセージと、レベルが足りない時に出るメッセージ以外はありません。
			} else {
				L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SKILL_POLYMORPH);

				L1PolyMorph.doPoly(pc, polyId, skillTemp.getBuffDuration(),
						L1PolyMorph.MORPH_BY_ITEMMAGIC);
				if (attacker.getId() != pc.getId()) {
					pc.sendPackets(new S_ServerMessage(SystemMessageId.$241, attacker.getName()));
				}
			}
		} else if (cha instanceof L1MonsterInstance) {
			L1MonsterInstance mob = (L1MonsterInstance) cha;
			if (mob.getLevel() < 50) {
				int npcId = mob.getNpcTemplate().get_npcId();
				if (npcId != 45338 && npcId != 45370 && npcId != 45456 // クロコダイル、バンディットボス、ネクロマンサー
						&& npcId != 45464 && npcId != 45473 && npcId != 45488 // セマ、バルタザール、カスパー
						&& npcId != 45497 && npcId != 45516 && npcId != 45529 // メルキオール、イフリート、ドレイク(DV)
						&& npcId != 45458) { // ドレイク(船長)
					L1Skills skillTemp = SkillsTable.getInstance().getTemplate(SKILL_POLYMORPH);
					L1PolyMorph.doPoly(mob, polyId, skillTemp.getBuffDuration(),
							L1PolyMorph.MORPH_BY_ITEMMAGIC);
				}
			}
		}
	}

	/** 變身藥水動作 */
	public static void Potion(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 41143) { // ラバーボーンヘッド変身ポーション
			polyId = 6086;
		} else if (itemId == 41144) { // ラバーボーンアーチャー変身ポーション
			polyId = 6087;
		} else if (itemId == 41145) { // ラバーボーンナイフ変身ポーション
			polyId = 6088;
		} else if (itemId == 49149 && pc.get_sex() == 0 && pc.isCrown()) { // シャルナの変身スクロール（レベル30）
			polyId = 6822;
		} else if (itemId == 49149 && pc.get_sex() == 1 && pc.isCrown()) {
			polyId = 6823;
		} else if (itemId == 49149 && pc.get_sex() == 0 && pc.isKnight()) {
			polyId = 6824;
		} else if (itemId == 49149 && pc.get_sex() == 1 && pc.isKnight()) {
			polyId = 6825;
		} else if (itemId == 49149 && pc.get_sex() == 0 && pc.isElf()) {
			polyId = 6826;
		} else if (itemId == 49149 && pc.get_sex() == 1 && pc.isElf()) {
			polyId = 6827;
		} else if (itemId == 49149 && pc.get_sex() == 0 && pc.isWizard()) {
			polyId = 6828;
		} else if (itemId == 49149 && pc.get_sex() == 1 && pc.isWizard()) {
			polyId = 6829;
		} else if (itemId == 49149 && pc.get_sex() == 0 && pc.isDarkelf()) {
			polyId = 6830;
		} else if (itemId == 49149 && pc.get_sex() == 1 && pc.isDarkelf()) {
			polyId = 6831;
		} else if (itemId == 49149 && pc.get_sex() == 0 && pc.isDragonKnight()) {
			polyId = 7139;
		} else if (itemId == 49149 && pc.get_sex() == 1 && pc.isDragonKnight()) {
			polyId = 7140;
		} else if (itemId == 49149 && pc.get_sex() == 0 && pc.isIllusionist()) {
			polyId = 7141;
		} else if (itemId == 49149 && pc.get_sex() == 1 && pc.isIllusionist()) {
			polyId = 7142;
		} else if (itemId == 49150 && pc.get_sex() == 0 && pc.isCrown()) { // シャルナの変身スクロール（レベル40）
			polyId = 6832;
		} else if (itemId == 49150 && pc.get_sex() == 1 && pc.isCrown()) {
			polyId = 6833;
		} else if (itemId == 49150 && pc.get_sex() == 0 && pc.isKnight()) {
			polyId = 6834;
		} else if (itemId == 49150 && pc.get_sex() == 1 && pc.isKnight()) {
			polyId = 6835;
		} else if (itemId == 49150 && pc.get_sex() == 0 && pc.isElf()) {
			polyId = 6836;
		} else if (itemId == 49150 && pc.get_sex() == 1 && pc.isElf()) {
			polyId = 6837;
		} else if (itemId == 49150 && pc.get_sex() == 0 && pc.isWizard()) {
			polyId = 6838;
		} else if (itemId == 49150 && pc.get_sex() == 1 && pc.isWizard()) {
			polyId = 6839;
		} else if (itemId == 49150 && pc.get_sex() == 0 && pc.isDarkelf()) {
			polyId = 6840;
		} else if (itemId == 49150 && pc.get_sex() == 1 && pc.isDarkelf()) {
			polyId = 6841;
		} else if (itemId == 49150 && pc.get_sex() == 0 && pc.isDragonKnight()) {
			polyId = 7143;
		} else if (itemId == 49150 && pc.get_sex() == 1 && pc.isDragonKnight()) {
			polyId = 7144;
		} else if (itemId == 49150 && pc.get_sex() == 0 && pc.isIllusionist()) {
			polyId = 7145;
		} else if (itemId == 49150 && pc.get_sex() == 1 && pc.isIllusionist()) {
			polyId = 7146;
		} else if (itemId == 49151 && pc.get_sex() == 0 && pc.isCrown()) { // シャルナの変身スクロール（レベル52）
			polyId = 6842;
		} else if (itemId == 49151 && pc.get_sex() == 1 && pc.isCrown()) {
			polyId = 6843;
		} else if (itemId == 49151 && pc.get_sex() == 0 && pc.isKnight()) {
			polyId = 6844;
		} else if (itemId == 49151 && pc.get_sex() == 1 && pc.isKnight()) {
			polyId = 6845;
		} else if (itemId == 49151 && pc.get_sex() == 0 && pc.isElf()) {
			polyId = 6846;
		} else if (itemId == 49151 && pc.get_sex() == 1 && pc.isElf()) {
			polyId = 6847;
		} else if (itemId == 49151 && pc.get_sex() == 0 && pc.isWizard()) {
			polyId = 6848;
		} else if (itemId == 49151 && pc.get_sex() == 1 && pc.isWizard()) {
			polyId = 6849;
		} else if (itemId == 49151 && pc.get_sex() == 0 && pc.isDarkelf()) {
			polyId = 6850;
		} else if (itemId == 49151 && pc.get_sex() == 1 && pc.isDarkelf()) {
			polyId = 6851;
		} else if (itemId == 49151 && pc.get_sex() == 0 && pc.isDragonKnight()) {
			polyId = 7147;
		} else if (itemId == 49151 && pc.get_sex() == 1 && pc.isDragonKnight()) {
			polyId = 7148;
		} else if (itemId == 49151 && pc.get_sex() == 0 && pc.isIllusionist()) {
			polyId = 7149;
		} else if (itemId == 49151 && pc.get_sex() == 1 && pc.isIllusionist()) {
			polyId = 7150;
		} else if (itemId == 49152 && pc.get_sex() == 0 && pc.isCrown()) { // シャルナの変身スクロール（レベル55）
			polyId = 6852;
		} else if (itemId == 49152 && pc.get_sex() == 1 && pc.isCrown()) {
			polyId = 6853;
		} else if (itemId == 49152 && pc.get_sex() == 0 && pc.isKnight()) {
			polyId = 6854;
		} else if (itemId == 49152 && pc.get_sex() == 1 && pc.isKnight()) {
			polyId = 6855;
		} else if (itemId == 49152 && pc.get_sex() == 0 && pc.isElf()) {
			polyId = 6856;
		} else if (itemId == 49152 && pc.get_sex() == 1 && pc.isElf()) {
			polyId = 6857;
		} else if (itemId == 49152 && pc.get_sex() == 0 && pc.isWizard()) {
			polyId = 6858;
		} else if (itemId == 49152 && pc.get_sex() == 1 && pc.isWizard()) {
			polyId = 6859;
		} else if (itemId == 49152 && pc.get_sex() == 0 && pc.isDarkelf()) {
			polyId = 6860;
		} else if (itemId == 49152 && pc.get_sex() == 1 && pc.isDarkelf()) {
			polyId = 6861;
		} else if (itemId == 49152 && pc.get_sex() == 0 && pc.isDragonKnight()) {
			polyId = 7151;
		} else if (itemId == 49152 && pc.get_sex() == 1 && pc.isDragonKnight()) {
			polyId = 7152;
		} else if (itemId == 49152 && pc.get_sex() == 0 && pc.isIllusionist()) {
			polyId = 7153;
		} else if (itemId == 49152 && pc.get_sex() == 1 && pc.isIllusionist()) {
			polyId = 7154;
		} else if (itemId == 49153 && pc.get_sex() == 0 && pc.isCrown()) { // シャルナの変身スクロール（レベル60）
			polyId = 6862;
		} else if (itemId == 49153 && pc.get_sex() == 1 && pc.isCrown()) {
			polyId = 6863;
		} else if (itemId == 49153 && pc.get_sex() == 0 && pc.isKnight()) {
			polyId = 6864;
		} else if (itemId == 49153 && pc.get_sex() == 1 && pc.isKnight()) {
			polyId = 6865;
		} else if (itemId == 49153 && pc.get_sex() == 0 && pc.isElf()) {
			polyId = 6866;
		} else if (itemId == 49153 && pc.get_sex() == 1 && pc.isElf()) {
			polyId = 6867;
		} else if (itemId == 49153 && pc.get_sex() == 0 && pc.isWizard()) {
			polyId = 6868;
		} else if (itemId == 49153 && pc.get_sex() == 1 && pc.isWizard()) {
			polyId = 6869;
		} else if (itemId == 49153 && pc.get_sex() == 0 && pc.isDarkelf()) {
			polyId = 6870;
		} else if (itemId == 49153 && pc.get_sex() == 1 && pc.isDarkelf()) {
			polyId = 6871;
		} else if (itemId == 49153 && pc.get_sex() == 0 && pc.isDragonKnight()) {
			polyId = 7155;
		} else if (itemId == 49153 && pc.get_sex() == 1 && pc.isDragonKnight()) {
			polyId = 7156;
		} else if (itemId == 49153 && pc.get_sex() == 0 && pc.isIllusionist()) {
			polyId = 7157;
		} else if (itemId == 49153 && pc.get_sex() == 1 && pc.isIllusionist()) {
			polyId = 7158;
		} else if (itemId == 49154 && pc.get_sex() == 0 && pc.isCrown()) { // シャルナの変身スクロール（レベル65）
			polyId = 6872;
		} else if (itemId == 49154 && pc.get_sex() == 1 && pc.isCrown()) {
			polyId = 6873;
		} else if (itemId == 49154 && pc.get_sex() == 0 && pc.isKnight()) {
			polyId = 6874;
		} else if (itemId == 49154 && pc.get_sex() == 1 && pc.isKnight()) {
			polyId = 6875;
		} else if (itemId == 49154 && pc.get_sex() == 0 && pc.isElf()) {
			polyId = 6876;
		} else if (itemId == 49154 && pc.get_sex() == 1 && pc.isElf()) {
			polyId = 6877;
		} else if (itemId == 49154 && pc.get_sex() == 0 && pc.isWizard()) {
			polyId = 6878;
		} else if (itemId == 49154 && pc.get_sex() == 1 && pc.isWizard()) {
			polyId = 6879;
		} else if (itemId == 49154 && pc.get_sex() == 0 && pc.isDarkelf()) {
			polyId = 6880;
		} else if (itemId == 49154 && pc.get_sex() == 1 && pc.isDarkelf()) {
			polyId = 6881;
		} else if (itemId == 49154 && pc.get_sex() == 0 && pc.isDragonKnight()) {
			polyId = 7159;
		} else if (itemId == 49154 && pc.get_sex() == 1 && pc.isDragonKnight()) {
			polyId = 7160;
		} else if (itemId == 49154 && pc.get_sex() == 0 && pc.isIllusionist()) {
			polyId = 7161;
		} else if (itemId == 49154 && pc.get_sex() == 1 && pc.isIllusionist()) {
			polyId = 7162;
		} else if (itemId == 49155 && pc.get_sex() == 0 && pc.isCrown()) { // シャルナの変身スクロール（レベル70）
			polyId = 6882;
		} else if (itemId == 49155 && pc.get_sex() == 1 && pc.isCrown()) {
			polyId = 6883;
		} else if (itemId == 49155 && pc.get_sex() == 0 && pc.isKnight()) {
			polyId = 6884;
		} else if (itemId == 49155 && pc.get_sex() == 1 && pc.isKnight()) {
			polyId = 6885;
		} else if (itemId == 49155 && pc.get_sex() == 0 && pc.isElf()) {
			polyId = 6886;
		} else if (itemId == 49155 && pc.get_sex() == 1 && pc.isElf()) {
			polyId = 6887;
		} else if (itemId == 49155 && pc.get_sex() == 0 && pc.isWizard()) {
			polyId = 6888;
		} else if (itemId == 49155 && pc.get_sex() == 1 && pc.isWizard()) {
			polyId = 6889;
		} else if (itemId == 49155 && pc.get_sex() == 0 && pc.isDarkelf()) {
			polyId = 6890;
		} else if (itemId == 49155 && pc.get_sex() == 1 && pc.isDarkelf()) {
			polyId = 6891;
		} else if (itemId == 49155 && pc.get_sex() == 0 && pc.isDragonKnight()) {
			polyId = 7163;
		} else if (itemId == 49155 && pc.get_sex() == 1 && pc.isDragonKnight()) {
			polyId = 7164;
		} else if (itemId == 49155 && pc.get_sex() == 0 && pc.isIllusionist()) {
			polyId = 7165;
		} else if (itemId == 49155 && pc.get_sex() == 1 && pc.isIllusionist()) {
			polyId = 7166;
		}
		L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
	}

	/** 變身卷軸動作 */
	public static boolean Scroll(L1PcInstance pc, int item_id, String s) {
		int awakeSkillId = pc.getAwakeSkillId();
		if (awakeSkillId == SKILL_AWAKEN_ANTHARAS || awakeSkillId == SKILL_AWAKEN_FAFURION
				|| awakeSkillId == SKILL_AWAKEN_VALAKAS) {
			pc.sendPackets(new S_ServerMessage(SystemMessageId.$1384));
			return false;
		}

		int time = 0;
		if (item_id == 40088 || item_id == 40096) { // 變身スクロール、象牙の塔の變身スクロール
			time = 1800;
		} else if (item_id == 140088) { // 祝福された變身スクロール
			time = 2100;
		}

		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		if (poly != null || s.equals("")) {
			if (s.equals("")) {
				if (pc.getTempCharGfx() == 6034 || pc.getTempCharGfx() == 6035) {
					return true;
				} else {
					pc.removeSkillEffect(SKILL_POLYMORPH);
					return true;
				}
			} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/** 變身鱗片動作 */
	public static void Scale(L1PcInstance pc, int itemId) {
		int polyId = 0;
		if (itemId == 41154) { // 闇の鱗
			polyId = 3101;
		} else if (itemId == 41155) { // 烈火の鱗
			polyId = 3126;
		} else if (itemId == 41156) { // 背德者の鱗
			polyId = 3888;
		} else if (itemId == 41157) { // 憎惡の鱗
			polyId = 3784;
		} else if (itemId == 49220) { // オーク密使変身スクロール
			polyId = 6984;
		}
		L1PolyMorph.doPoly(pc, polyId, 600, L1PolyMorph.MORPH_BY_ITEMMAGIC);
	}

}
