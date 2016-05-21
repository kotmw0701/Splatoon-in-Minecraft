package jp.kotmw.splatoon;

import org.bitbucket.ucchy.undine.tellraw.ClickEventType;
import org.bitbucket.ucchy.undine.tellraw.MessageComponent;
import org.bitbucket.ucchy.undine.tellraw.MessageParts;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageButton
{
	private Metrics data = new Metrics();
	//private static final String BR = System.getProperty("line.separator");
	private static final String BR = "\n";


	/**
	 * ボタンを作成しやすくする。
	 *
	 * @param ms メッセージ送信先
	 * @param Text 表示するテキスト
	 * @param HoverText 表示するホバーテキスト
	 * @param Value ボタンを押した時に出てくる文字列(Buttonによって変化)
	 * @param Button クリックされた時に行われる動作
	 */
	public void ButtonCreate(Player ms,String Text,String HoverText,String HoverText2,String HoverText3, String HoverText4, String Value,ClickEventType Button)
	{
		MessageComponent msg = new MessageComponent();
		MessageParts button1 = new MessageParts("["+Text+"]", ChatColor.AQUA);
		button1.addHoverText(HoverText + BR);
		button1.addHoverText(HoverText2 + BR);
		button1.addHoverText(HoverText3 + BR);
		button1.addHoverText(HoverText4);
		button1.setClickEvent(Button, Value);
		msg.addParts(button1);
		msg.send(ms);
	}

	public void ButtonCreate(Player ms,String Text,String HoverText,String HoverText2,String HoverText3,String Value,ClickEventType Button)
	{
		MessageComponent msg = new MessageComponent();
		MessageParts button1 = new MessageParts("["+Text+"]", ChatColor.AQUA);
		button1.addHoverText(HoverText + BR);
		button1.addHoverText(HoverText2 + BR);
		button1.addHoverText(HoverText3);
		button1.setClickEvent(Button, Value);
		msg.addParts(button1);
		msg.send(ms);
	}
	public void ButtonCreate(Player ms,String Text,String HoverText,String HoverText2,String Value,ClickEventType Button)
	{
		MessageComponent msg = new MessageComponent();
		MessageParts button1 = new MessageParts("["+Text+"]", ChatColor.AQUA);
		button1.addHoverText(HoverText + BR);
		button1.addHoverText(HoverText2);
		button1.setClickEvent(Button, Value);
		msg.addParts(button1);
		msg.send(ms);
	}

	public void ButtonCreate(Player ms,String Text,String HoverText,String Value,ClickEventType Button)
	{
		MessageComponent msg = new MessageComponent();
		MessageParts button1 = new MessageParts("["+Text+"]", ChatColor.AQUA);
		button1.addHoverText(HoverText);
		button1.setClickEvent(Button, Value);
		msg.addParts(button1);
		msg.send(ms);
	}

	public void ButtonCreate_Vote(Player ms,String Text,String HoverText,String Value,ClickEventType Button)
	{
		MessageComponent msg = new MessageComponent();
		MessageParts button1 = new MessageParts("                              ["+Text+"]", ChatColor.AQUA);
		button1.addHoverText(HoverText);
		button1.setClickEvent(Button, Value);
		msg.addParts(button1);
		msg.send(ms);
	}
    /**
     * ボタンを作成しやすくする。
     *
     * @param ms メッセージ送信先
     * @param Text 表示するテキスト
     * @param Value ボタンを押した時に出てくる文字列(Buttonによって変化)
     * @param Button クリックされた時に行われる動作
     */
    public void ButtonCreate(Player ms,String Text,String Value,ClickEventType Button)
    {
        MessageComponent msg = new MessageComponent();
        MessageParts button1 = new MessageParts("["+Text+"]", ChatColor.AQUA);
        button1.setClickEvent(Button, Value);
        msg.addText(data.Pprefix);
        msg.addParts(button1);
        msg.send(ms);
    }

}
