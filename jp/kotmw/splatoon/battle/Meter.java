/**
 * @author kotmw0701
 * @license LGPLv3
 * @copyright Copyright kotmw 2015
 *
 */package jp.kotmw.splatoon.battle;

import jp.kotmw.splatoon.ColorSelect;

import org.bukkit.ChatColor;

public class Meter
{
	public static String sendMeter(String arena, int i)
	{
		if(i == 130)
			return MeterText(arena, 0, 98);
		else if(i == 125)
			return MeterText(arena, 1, 97);
		else if(i == 120)
			return MeterText(arena, 2, 96);
		else if(i == 115)
			return MeterText(arena, 3, 95);
		else if(i == 110)
			return MeterText(arena, 4, 94);
		else if(i == 105)
			return MeterText(arena, 5, 93);
		else if(i == 100)
			return MeterText(arena, 6, 92);
		else if(i == 95)
			return MeterText(arena, 7, 91);
		else if(i == 90)
			return MeterText(arena, 8, 90);
		else if(i == 85)
			return MeterText(arena, 9, 89);
		else if(i == 80)
			return MeterText(arena, 10, 88);
		else if(i == 75)
			return MeterText(arena, 11, 87);
		else if(i == 70)
			return MeterText(arena, 12, 86);
		else if(i == 65)
			return MeterText(arena, 13, 85);
		else if(i == 60)
			return MeterText(arena, 14, 84);
		else if(i == 55)
			return MeterText(arena, 15, 83);
		else if(i == 50)
			return MeterText(arena, 16, 82);
		else if(i == 45)
			return MeterText(arena, 17, 81);
		else if(i == 40)
			return MeterText(arena, 18, 80);
		else if(i == 35)
			return MeterText(arena, 19, 79);
		else if(i == 30)
			return MeterText(arena, 20, 78);
		else if(i == 25)
			return MeterText(arena, 21, 77);
		else if(i == 20)
			return MeterText(arena, 22, 76);
		else if(i == 15)
			return MeterText(arena, 23, 75);
		else if(i == 10)
			return MeterText(arena, 24, 74);
		else if(i == 5)
			return MeterText(arena, 25, 73);
		else if(i == 0)
			return MeterText(arena, 26, 72);
		return null;
	}

	public static String KnockOut(String arena, int i)
	{
		if(i == 1)
			return MeterText2(arena, 99, 99);
		else if(i == 2)
			return MeterText2(arena, 0, 0);
		return null;
	}

	private static String MeterText(String arena, int i, int ii)
	{
		String base = "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||";
		String space = "            ";

		return ColorSelect.color_team1_prefix(arena) +base.substring(0, i)+ ChatColor.GRAY +base.substring(i + 1, ii)+ ColorSelect.color_team2_prefix(arena) +base.substring(ii + 1, 99)+ space;
	}

	private static String MeterText2(String arena, int i, int ii)
	{
		String base = "||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||";
		String space = "            ";
		return ColorSelect.color_team1_prefix(arena) +base.substring(0, i)+ ColorSelect.color_team2_prefix(arena) +base.substring(ii, 99)+ space;
	}

	public static String ParceMeter(String arena, double amount)
	{
		if(0.0<=amount && amount<=0.9)
			return MeterText2(arena, 0, 1);
		else if(1.0<=amount && amount<=1.9)
			return MeterText2(arena, 1, 2);
		else if(2.0<=amount && amount<=2.9)
			return MeterText2(arena, 2, 3);
		else if(3.0<=amount && amount<=3.9)
			return MeterText2(arena, 3, 4);
		else if(4.0<=amount && amount<=4.9)
			return MeterText2(arena, 4, 5);
		else if(5.0<=amount && amount<=5.9)
			return MeterText2(arena, 5, 6);
		else if(6.0<=amount && amount<=6.9)
			return MeterText2(arena, 6, 7);
		else if(7.0<=amount && amount<=7.9)
			return MeterText2(arena, 7, 8);
		else if(8.0<=amount && amount<=8.9)
			return MeterText2(arena, 8, 9);
		else if(9.0<=amount && amount<=9.9)
			return MeterText2(arena, 9, 10);

		else if(10.0<=amount && amount<=10.9)
			return MeterText2(arena, 10, 11);
		else if(11.0<=amount && amount<=11.9)
			return MeterText2(arena, 11, 12);
		else if(12.0<=amount && amount<=12.9)
			return MeterText2(arena, 12, 13);
		else if(13.0<=amount && amount<=13.9)
			return MeterText2(arena, 13, 14);
		else if(14.0<=amount && amount<=14.9)
			return MeterText2(arena, 14, 15);
		else if(15.0<=amount && amount<=15.9)
			return MeterText2(arena, 15, 16);
		else if(16.0<=amount && amount<=16.9)
			return MeterText2(arena, 16, 17);
		else if(17.0<=amount && amount<=17.9)
			return MeterText2(arena, 17, 18);
		else if(18.0<=amount && amount<=18.9)
			return MeterText2(arena, 18, 19);
		else if(19.0<=amount && amount<=19.9)
			return MeterText2(arena, 19, 20);

		else if(20.0<=amount && amount<=20.9)
			return MeterText2(arena, 20, 21);
		else if(21.0<=amount && amount<=21.9)
			return MeterText2(arena, 21, 22);
		else if(22.0<=amount && amount<=22.9)
			return MeterText2(arena, 22, 23);
		else if(23.0<=amount && amount<=23.9)
			return MeterText2(arena, 23, 24);
		else if(24.0<=amount && amount<=24.9)
			return MeterText2(arena, 24, 25);
		else if(25.0<=amount && amount<=25.9)
			return MeterText2(arena, 25, 26);
		else if(26.0<=amount && amount<=26.9)
			return MeterText2(arena, 26, 27);
		else if(27.0<=amount && amount<=27.9)
			return MeterText2(arena, 27, 28);
		else if(28.0<=amount && amount<=28.9)
			return MeterText2(arena, 28, 29);
		else if(29.0<=amount && amount<=29.9)
			return MeterText2(arena, 29, 30);

		else if(30.0<=amount && amount<=30.9)
			return MeterText2(arena, 30, 31);
		else if(31.0<=amount && amount<=31.9)
			return MeterText2(arena, 31, 32);
		else if(32.0<=amount && amount<=32.9)
			return MeterText2(arena, 32, 33);
		else if(33.0<=amount && amount<=33.9)
			return MeterText2(arena, 33, 34);
		else if(34.0<=amount && amount<=34.9)
			return MeterText2(arena, 34, 35);
		else if(35.0<=amount && amount<=35.9)
			return MeterText2(arena, 35, 36);
		else if(36.0<=amount && amount<=36.9)
			return MeterText2(arena, 36, 37);
		else if(37.0<=amount && amount<=37.9)
			return MeterText2(arena, 37, 38);
		else if(38.0<=amount && amount<=38.9)
			return MeterText2(arena, 38, 39);
		else if(39.0<=amount && amount<=39.9)
			return MeterText2(arena, 39, 40);

		else if(40.0<=amount && amount<=40.9)
			return MeterText2(arena, 40, 41);
		else if(41.0<=amount && amount<=41.9)
			return MeterText2(arena, 41, 42);
		else if(42.0<=amount && amount<=42.9)
			return MeterText2(arena, 42, 43);
		else if(43.0<=amount && amount<=43.9)
			return MeterText2(arena, 43, 44);
		else if(44.0<=amount && amount<=44.9)
			return MeterText2(arena, 44, 45);
		else if(45.0<=amount && amount<=45.9)
			return MeterText2(arena, 45, 46);
		else if(46.0<=amount && amount<=46.9)
			return MeterText2(arena, 46, 47);
		else if(47.0<=amount && amount<=47.9)
			return MeterText2(arena, 47, 48);
		else if(48.0<=amount && amount<=48.9)
			return MeterText2(arena, 48, 49);
		else if(49.0<=amount && amount<=49.9)
			return MeterText2(arena, 49, 50);


		else if(50.0<=amount && amount<=50.9)
			return MeterText2(arena, 50, 51);
		else if(51.0<=amount && amount<=51.9)
			return MeterText2(arena, 51, 52);
		else if(52.0<=amount && amount<=52.9)
			return MeterText2(arena, 52, 53);
		else if(53.0<=amount && amount<=53.9)
			return MeterText2(arena, 53, 54);
		else if(54.0<=amount && amount<=54.9)
			return MeterText2(arena, 54, 55);
		else if(55.0<=amount && amount<=55.9)
			return MeterText2(arena, 55, 56);
		else if(56.0<=amount && amount<=56.9)
			return MeterText2(arena, 56, 57);
		else if(57.0<=amount && amount<=57.9)
			return MeterText2(arena, 57, 58);
		else if(58.0<=amount && amount<=58.9)
			return MeterText2(arena, 58, 59);
		else if(59.0<=amount && amount<=59.9)
			return MeterText2(arena, 59, 60);

		else if(60.0<=amount && amount<=60.9)
			return MeterText2(arena, 60, 61);
		else if(61.0<=amount && amount<=61.9)
			return MeterText2(arena, 61, 62);
		else if(62.0<=amount && amount<=62.9)
			return MeterText2(arena, 62, 63);
		else if(63.0<=amount && amount<=63.9)
			return MeterText2(arena, 63, 64);
		else if(64.0<=amount && amount<=64.9)
			return MeterText2(arena, 64, 65);
		else if(65.0<=amount && amount<=65.9)
			return MeterText2(arena, 65, 66);
		else if(66.0<=amount && amount<=66.9)
			return MeterText2(arena, 66, 67);
		else if(67.0<=amount && amount<=67.9)
			return MeterText2(arena, 67, 68);
		else if(68.0<=amount && amount<=68.9)
			return MeterText2(arena, 68, 69);
		else if(69.0<=amount && amount<=69.9)
			return MeterText2(arena, 69, 70);

		else if(70.0<=amount && amount<=70.9)
			return MeterText2(arena, 70, 71);
		else if(71.0<=amount && amount<=71.9)
			return MeterText2(arena, 71, 72);
		else if(72.0<=amount && amount<=72.9)
			return MeterText2(arena, 72, 73);
		else if(73.0<=amount && amount<=73.9)
			return MeterText2(arena, 73, 74);
		else if(74.0<=amount && amount<=74.9)
			return MeterText2(arena, 74, 75);
		else if(75.0<=amount && amount<=75.9)
			return MeterText2(arena, 75, 76);
		else if(76.0<=amount && amount<=76.9)
			return MeterText2(arena, 76, 77);
		else if(77.0<=amount && amount<=77.9)
			return MeterText2(arena, 77, 78);
		else if(78.0<=amount && amount<=78.9)
			return MeterText2(arena, 78, 79);
		else if(79.0<=amount && amount<=79.9)
			return MeterText2(arena, 79, 80);

		else if(80.0<=amount && amount<=80.9)
			return MeterText2(arena, 80, 81);
		else if(81.0<=amount && amount<=81.9)
			return MeterText2(arena, 81, 82);
		else if(82.0<=amount && amount<=82.9)
			return MeterText2(arena, 82, 83);
		else if(83.0<=amount && amount<=83.9)
			return MeterText2(arena, 83, 84);
		else if(84.0<=amount && amount<=84.9)
			return MeterText2(arena, 84, 85);
		else if(85.0<=amount && amount<=85.9)
			return MeterText2(arena, 85, 86);
		else if(86.0<=amount && amount<=86.9)
			return MeterText2(arena, 86, 87);
		else if(87.0<=amount && amount<=87.9)
			return MeterText2(arena, 87, 88);
		else if(88.0<=amount && amount<=88.9)
			return MeterText2(arena, 88, 89);
		else if(89.0<=amount && amount<=89.9)
			return MeterText2(arena, 89, 90);

		else if(90.0<=amount && amount<=90.9)
			return MeterText2(arena, 90, 91);
		else if(91.0<=amount && amount<=91.9)
			return MeterText2(arena, 91, 92);
		else if(92.0<=amount && amount<=92.9)
			return MeterText2(arena, 92, 93);
		else if(93.0<=amount && amount<=93.9)
			return MeterText2(arena, 93, 94);
		else if(94.0<=amount && amount<=94.9)
			return MeterText2(arena, 94, 95);
		else if(95.0<=amount && amount<=95.9)
			return MeterText2(arena, 95, 96);
		else if(96.0<=amount && amount<=96.9)
			return MeterText2(arena, 96, 97);
		else if(97.0<=amount && amount<=97.9)
			return MeterText2(arena, 97, 98);
		else if(98.0<=amount && amount<=98.9)
			return MeterText2(arena, 98, 99);
		else if(99.0<=amount && amount<=99.9)
			return MeterText2(arena, 99, 99);

		else if(100.0<=amount)
			return MeterText2(arena, 99, 99);
		return null;
	}
}
