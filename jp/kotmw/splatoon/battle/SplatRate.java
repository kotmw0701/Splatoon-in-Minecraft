/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */
package jp.kotmw.splatoon.battle;

import jp.kotmw.splatoon.Metrics;
import jp.kotmw.splatoon.Splatoon;

public class SplatRate
{
	Metrics data = new Metrics();
	String player;

	public SplatRate(String player)
	{
		this.player = player;
	}

	public int getWinCount()
	{
		return Splatoon.files.rateconfig.getInt("Player." + player + ".Win");
	}

	public int getLoseCount()
	{
		return Splatoon.files.rateconfig.getInt("Player." + player + ".Lose");
	}

	public int getWinStreak()
	{
		return Splatoon.files.rateconfig.getInt("Player." + player + ".WinStreak");
	}

	public int getMaximumWinStreak()
	{
		return Splatoon.files.rateconfig.getInt("Player." + player + ".MaximumWinStreak");
	}

	public boolean getfinalwin()
	{
		return Splatoon.files.rateconfig.getBoolean("Player." + player + ".FinalWin");
	}
}
