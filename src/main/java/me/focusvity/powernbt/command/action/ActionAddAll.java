package me.focusvity.powernbt.command.action;

import me.focusvity.powernbt.container.Container;
import me.focusvity.powernbt.util.Caller;
import me.focusvity.powernbt.util.Query;
import me.focusvity.powernbt.util.Viewer;
import net.minecraft.server.v1_16_R3.*;

public class ActionAddAll extends Action
{

    private final Caller caller;
    private final Argument firstArg;
    private final Argument secondArg;

    public ActionAddAll(Caller caller, String firstObject, String firstParam, String secondObject, String secondParam)
    {
        this.caller = caller;
        this.firstArg = new Argument(caller, firstObject, firstParam);
        this.secondArg = new Argument(caller, secondObject, secondParam);
    }

    @Override
    public void execute() throws Exception
    {
        if (firstArg.needPrepare())
        {
            firstArg.prepare(this, null, null);
            return;
        }
        Container firstContainer = firstArg.getContainer();
        Query firstQuery = secondArg.getQuery();
        if (secondArg.needPrepare())
        {
            secondArg.prepare(this, firstContainer, firstQuery);
            return;
        }
        Container secondContainer = secondArg.getContainer();
        Query secondQuery = secondArg.getQuery();
        NBTBase firstBase = firstQuery.get(firstContainer.getCustomTag());
        NBTBase secondBase = secondQuery.get(secondContainer.getCustomTag());
        if (firstBase == null)
        {
            if (secondBase instanceof NBTTagCompound)
            {
                firstBase = new NBTTagCompound();
            }
            if (secondBase instanceof NBTTagList)
            {
                firstBase = new NBTTagList();
            }
            if (secondBase instanceof NBTTagByteArray)
            {
                firstBase = new NBTTagByteArray(new byte[0]);
            }
            if (secondBase instanceof NBTTagIntArray)
            {
                firstBase = new NBTTagIntArray(new int[0]);
            }
            if (secondBase instanceof NBTTagLongArray)
            {
                firstBase = new NBTTagLongArray(new long[0]);
            }
            if (secondBase instanceof NBTNumber)
            {
                firstBase = secondBase.clone();
            }
        }
        if (firstBase instanceof NBTTagCompound && secondBase instanceof NBTTagCompound)
        {
            NBTTagCompound firstCompound = (NBTTagCompound) firstBase;
            NBTTagCompound secondCompound = (NBTTagCompound) secondBase;
            firstCompound.a(secondCompound);
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), firstCompound));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondCompound, false, false));
        }
        else if (firstBase instanceof NBTTagList && secondBase instanceof NBTTagList)
        {
            NBTTagList firstList = (NBTTagList) firstBase;
            NBTTagList secondList = (NBTTagList) secondBase;
            firstList.addAll(secondList);
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), firstList));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondList, false, false));
        }
        else if (firstBase instanceof NBTTagByteArray && secondBase instanceof NBTTagByteArray)
        {
            NBTTagByteArray firstArray = (NBTTagByteArray) firstBase;
            NBTTagByteArray secondArray = (NBTTagByteArray) secondBase;
            firstArray.addAll(secondArray);
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), firstArray));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondArray, false, false));
        }
        else if (firstBase instanceof NBTTagIntArray && secondBase instanceof NBTTagIntArray)
        {
            NBTTagIntArray firstArray = (NBTTagIntArray) firstBase;
            NBTTagIntArray secondArray = (NBTTagIntArray) secondBase;
            firstArray.addAll(secondArray);
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), firstArray));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondArray, false, false));
        }
        else if (firstBase instanceof NBTTagLongArray && secondBase instanceof NBTTagLongArray)
        {
            NBTTagLongArray firstArray = (NBTTagLongArray) firstBase;
            NBTTagLongArray secondArray = (NBTTagLongArray) secondBase;
            firstArray.addAll(secondArray);
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), firstArray));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondArray, false, false));
        }
        else if (firstBase instanceof NBTTagString && secondBase instanceof NBTTagString)
        {
            NBTTagString firstString = (NBTTagString) firstBase;
            NBTTagString secondString = (NBTTagString) secondBase;
            firstString.asString().concat(secondString.asString());
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), firstString));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondString, false, false));
        }
        else if (firstBase instanceof NBTTagByte && secondBase instanceof NBTTagByte)
        {
            NBTTagByte firstByte = (NBTTagByte) firstBase;
            NBTTagByte secondByte = (NBTTagByte) secondBase;
            NBTTagByte b = NBTTagByte.a((byte) (firstByte.asLong() + secondByte.asLong()));
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), b));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondByte, false, false));
        }
        else if (firstBase instanceof NBTTagShort && secondBase instanceof NBTTagShort)
        {
            NBTTagShort firstShort = (NBTTagShort) firstBase;
            NBTTagShort secondShort = (NBTTagShort) secondBase;
            NBTTagShort b = NBTTagShort.a((short) (firstShort.asLong() + secondShort.asLong()));
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), b));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondShort, false, false));
        }
        else if (firstBase instanceof NBTTagInt && secondBase instanceof NBTTagInt)
        {
            NBTTagInt firstInt = (NBTTagInt) firstBase;
            NBTTagInt secondInt = (NBTTagInt) secondBase;
            NBTTagInt b = NBTTagInt.a((int) (firstInt.asLong() + secondInt.asLong()));
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), b));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondInt, false, false));
        }
        else if (firstBase instanceof NBTTagLong && secondBase instanceof NBTTagLong)
        {
            NBTTagLong firstLong = (NBTTagLong) firstBase;
            NBTTagLong secondLong = (NBTTagLong) secondBase;
            NBTTagLong b = NBTTagLong.a(firstLong.asLong() + secondLong.asLong());
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), b));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondLong, false, false));
        }
        else if (firstBase instanceof NBTTagFloat && secondBase instanceof NBTTagFloat)
        {
            NBTTagFloat firstFloat = (NBTTagFloat) firstBase;
            NBTTagFloat secondFloat = (NBTTagFloat) secondBase;
            NBTTagFloat b = NBTTagFloat.a((float) (firstFloat.asDouble() + secondFloat.asDouble()));
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), b));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondFloat, false, false));
        }
        else if (firstBase instanceof NBTTagDouble && secondBase instanceof NBTTagDouble)
        {
            NBTTagDouble firstDouble = (NBTTagDouble) firstBase;
            NBTTagDouble secondDouble = (NBTTagDouble) secondBase;
            NBTTagDouble b = NBTTagDouble.a(firstDouble.asDouble() + secondDouble.asDouble());
            firstContainer.setCustomTag(firstQuery.set(firstContainer.getCustomTag(), b));
            caller.send("Values added: " + Viewer.getShortValueWithPrefix(secondDouble, false, false));
        }
        else
        {
            caller.send("Cannot add tags");
        }
    }
}
