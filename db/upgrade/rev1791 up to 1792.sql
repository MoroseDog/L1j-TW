INSERT INTO `armor` VALUES
(21107, 'ウッドン ジャケット', '$798', 'armor', 'wood', 30000, 51, 9, 0, -1, 4, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);

delete from npc where npcid = 70840 ;
INSERT INTO `npc` VALUES
(70840, 'ロビンウッド', 'ロビンウッド', '', 'L1Merchant', 916, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '', 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 14, 0, 1, 0),
(71256, 'ロビンフッド', '$2574', '', 'L1Merchant', 916, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, '', 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 14, 0, 1, 0);

INSERT INTO `npcaction` VALUES
(71256, 'robinhood1', 'robinhood1', '', '');

INSERT INTO `spawnlist_npc` VALUES
(87368, 'ロビンフッド', 1, 71256, 33031, 32344, 0, 0, 5, 0, 4, 100);