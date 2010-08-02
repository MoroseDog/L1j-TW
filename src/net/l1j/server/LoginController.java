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
package net.l1j.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.l1j.server.model.id.SystemMessageId;
import net.l1j.server.serverpackets.S_ServerMessage;
import net.l1j.thread.ThreadPoolManager;

public class LoginController {
	private Map<String, ClientThread> _accounts = new ConcurrentHashMap<String, ClientThread>();

	private static LoginController _instance;

	private int _maxAllowedOnlinePlayers;

	private LoginController() {
	}

	public static LoginController getInstance() {
		if (_instance == null) {
			_instance = new LoginController();
		}
		return _instance;
	}

	public ClientThread[] getAllAccounts() {
		return _accounts.values().toArray(new ClientThread[_accounts.size()]);
	}

	public int getOnlinePlayerCount() {
		return _accounts.size();
	}

	public int getMaxAllowedOnlinePlayers() {
		return _maxAllowedOnlinePlayers;
	}

	public void setMaxAllowedOnlinePlayers(int maxAllowedOnlinePlayers) {
		_maxAllowedOnlinePlayers = maxAllowedOnlinePlayers;
	}

	private void kickClient(final ClientThread client) {
		if (client == null) {
			return;
		}

		ThreadPoolManager.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				if (client.getActiveChar() != null) {
					client.getActiveChar().sendPackets(new S_ServerMessage(SystemMessageId.$357));
				}

				try {
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				client.kick();
			}
		});
	}

	public synchronized void login(ClientThread client, Account account) throws GameServerFullException, AccountAlreadyLoginException {
		if (!account.isValid()) {
			// パスワード認証がされていない、あるいは認証に失敗したアカウントが指定された。
			// このコードは、バグ檢出の為にのみ存在する。
			throw new IllegalArgumentException("帳號尚未通過認證!!");
		}
		if ((getMaxAllowedOnlinePlayers() <= getOnlinePlayerCount()) && !account.isGameMaster()) {
			throw new GameServerFullException();
		}
		if (_accounts.containsKey(account.getName())) {
			kickClient(_accounts.remove(account.getName()));
			throw new AccountAlreadyLoginException();
		}

		_accounts.put(account.getName(), client);
	}

	public synchronized boolean logout(ClientThread client) {
		if (client.getAccountName() == null) {
			return false;
		} else {
			client.offline();
		}
		return _accounts.remove(client.getAccountName()) != null;
	}
}
