package me.focusvity.powernbt.util;

import me.focusvity.powernbt.exception.NBTTagNotFound;
import me.focusvity.powernbt.exception.NBTTagUnexpectedType;
import net.minecraft.server.v1_16_R3.NBTBase;
import net.minecraft.server.v1_16_R3.NBTList;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;

import java.util.*;

public class Query
{

    private List<Object> values = new ArrayList<>();

    public Query(Object... nodes)
    {
        Arrays.stream(nodes).forEach(node ->
        {
            if (node instanceof String || node instanceof Integer)
            {
                values.add(node);
            }
            else
            {
                throw new RuntimeException("Invalid node: " + node);
            }
        });
    }

    public Query(List<Object> nodes)
    {
        nodes.forEach(node ->
        {
            if (node instanceof String || node instanceof Integer)
            {
                values.add(node);
            }
            else
            {
                throw new RuntimeException("Invalid node: " + node);
            }
        });
    }

    public static Query fromString(String string)
    {
        if (string == null || string.isEmpty())
        {
            return new Query();
        }
        LinkedList<Object> v = new LinkedList<>();
        Queue<Character> characters = new LinkedList<>();
        StringBuilder buffer = new StringBuilder();
        for (char c : string.toCharArray())
        {
            characters.add(c);
        }
        byte mode = 0;
        tokenizer:
        while (true)
        {
            Character c = characters.poll();
            switch (mode)
            {
                case 0:
                {
                    if (c == null)
                    {
                        if (buffer.length() != 0)
                        {
                            v.add(buffer.toString());
                        }
                        break tokenizer;
                    }
                    else if (c == '.')
                    {
                        if (buffer.length() != 0)
                        {
                            v.add(buffer.toString());
                            buffer = new StringBuilder();
                        }
                    }
                    else if (c == '\"')
                    {
                        mode = 1;
                    }
                    else if (c == '[')
                    {
                        if (buffer.length() != 0)
                        {
                            v.add(buffer.toString());
                            buffer = new StringBuilder();
                        }
                        mode = 2;
                    }
                    else if (c == ']')
                    {
                        throw new RuntimeException("Invalid Query Format: " + string);
                    }
                    else
                    {
                        buffer.append(c);
                    }
                    break;
                }
                case 1:
                {
                    if (c == null)
                    {
                        throw new RuntimeException("Invalid Query Format: " + string);
                    }
                    if (c == '\\')
                    {
                        buffer.append(c);
                        Character t = characters.poll();
                        if (t == null)
                        {
                            throw new RuntimeException("Invalid Query Format: " + string);
                        }
                        buffer.append(t);
                    }
                    else if (c == '"')
                    {
                        if (buffer.length() != 0)
                        {
                            v.add(StringParser.parse(buffer.toString()));
                            buffer = new StringBuilder();
                        }
                        mode = 0;
                    }
                    else
                    {
                        buffer.append(c);
                    }
                    break;
                }
                case 2:
                {
                    if (c == null)
                    {
                        throw new RuntimeException("Invalid Query Format: " + string);
                    }
                    else if (c == ']')
                    {
                        String t = buffer.toString();
                        int r = -1;
                        if (!t.isEmpty())
                        {
                            r = Integer.parseInt(t);
                        }
                        v.add(r);
                        buffer = new StringBuilder();
                        mode = 0;
                    }
                    else if (c.toString().matches("[0-9]"))
                    {
                        buffer.append(c);
                    }
                    else
                    {
                        throw new RuntimeException("Invalid Query Format: " + string);
                    }
                    break;
                }
            }
        }
        Query q = new Query();
        q.values = v;
        return q;
    }

    public List<Object> getValues()
    {
        return new ArrayList<>(values);
    }

    public boolean isEmpty()
    {
        return values.isEmpty();
    }

    public Query getParent()
    {
        if (isEmpty())
        {
            return null;
        }
        List<Object> v = getValues();
        v.remove(v.size() - 1);
        return new Query(v);
    }

    public Queue<Object> getQueue()
    {
        return new LinkedList<>(values);
    }

    public NBTBase remove(NBTBase root) throws NBTTagNotFound
    {
        if (root == null)
        {
            return null;
        }
        root = root.clone();
        if (isEmpty())
        {
            return null;
        }
        NBTBase current = root;
        Queue<Object> queue = getQueue();
        while (true)
        {
            if (queue.size() == 1)
            {
                Object t = queue.poll();
                if (current instanceof NBTTagCompound && t instanceof String)
                {
                    NBTTagCompound compound = (NBTTagCompound) current;
                    String string = (String) t;
                    compound.remove(string);
                    return root;
                }
                else if (current instanceof NBTTagList && t instanceof Integer)
                {
                    NBTTagList list = (NBTTagList) current;
                    int i = (Integer) t;
                    if (i == -1)
                    {
                        i = list.size() - 1;
                    }
                    list.remove(i);
                    return root;
                }
                else if (current instanceof NBTList && t instanceof Integer)
                {
                    NBTList list = (NBTList) current;
                    int i = (Integer) t;
                    if (i == -1)
                    {
                        i = list.size() - 1;
                    }
                    list.remove(i);
                    return root;
                }
                else
                {
                    throw new NBTTagNotFound(current, t);
                }
            }
            Object t = queue.poll();
            if (current == null)
            {
                throw new NBTTagNotFound(null, t);
            }
            if (current instanceof NBTTagCompound && t instanceof String)
            {
                NBTTagCompound compound = (NBTTagCompound) current;
                String string = (String) t;
                if (!compound.hasKey(string))
                {
                    throw new NBTTagNotFound(compound, t);
                }
                current = compound.get(string);
            }
            if (current instanceof NBTTagList && t instanceof Integer)
            {
                NBTTagList list = (NBTTagList) current;
                int i = (Integer) t;
                if (i == -1)
                {
                    i = list.size() - 1;
                }
                current = list.get(i);
            }
            else
            {
                throw new NBTTagNotFound(current, t);
            }
        }
    }

    public NBTBase get(NBTBase root) throws NBTTagNotFound
    {
        Queue<Object> queue = getQueue();
        NBTBase current = root;
        while (true)
        {
            Object t = queue.poll();
            if (t == null || current == null)
            {
                return current;
            }
            if (current instanceof NBTTagCompound && t instanceof String)
            {
                NBTTagCompound compound = (NBTTagCompound) current;
                String string = (String) t;
                if (!compound.hasKey(string))
                {
                    return null;
                }
                current = compound.get(string);
            }
            if (current instanceof NBTTagList && t instanceof Integer)
            {
                NBTTagList list = (NBTTagList) current;
                int i = (Integer) t;
                if (i == -1)
                {
                    i = list.size() - 1;
                }
                current = list.get(i);
            }
            if (current instanceof NBTList && t instanceof Integer)
            {
                NBTList list = (NBTList) current;
                int i = (Integer) t;
                if (i == -1)
                {
                    i = list.size() - 1;
                }
                current = (NBTBase) list.get(i);
            }
            else
            {
                throw new NBTTagNotFound(current, t);
            }
        }
    }

    private NBTBase call(NBTBase root) throws NBTTagNotFound
    {
        return get(root);
    }

    public NBTBase set(NBTBase root, NBTBase value) throws RuntimeException, NBTTagNotFound, NBTTagUnexpectedType
    {
        if (isEmpty())
        {
            return root.clone();
        }
        Queue<Object> queue = getQueue();
        if (root == null)
        {
            Object z = queue.peek();
            if (z instanceof String)
            {
                root = new NBTTagCompound();
            }
            else if (z instanceof Integer)
            {
                root = new NBTTagList();
            }
        }
        else
        {
            root = root.clone();
        }
        NBTBase current = root;
        while (true)
        {
            if (queue.size() == 1)
            {
                Object t = queue.poll();
                if (current instanceof NBTTagCompound && t instanceof String)
                {
                    NBTTagCompound compound = (NBTTagCompound) current;
                    String string = (String) t;
                    compound.set(string, value.clone());
                    return root;
                }
                else if (current instanceof NBTTagList && t instanceof Integer)
                {
                    NBTTagList list = (NBTTagList) current;
                    int i = (Integer) t;
                    if (i == -1)
                    {
                        list.add(value.clone());
                    }
                    else
                    {
                        list.set(i, value.clone());
                    }
                    return root;
                }
                else if (current instanceof NBTList && t instanceof Integer)
                {
                    NBTList list = (NBTList) current;
                    int i = (Integer) t;
                    if (i == -1)
                    {
                        i = list.size() - 1;
                    }
                    if (!(value instanceof NBTList))
                    {
                        throw new NBTTagUnexpectedType(value, NBTList.class);
                    }
                    list.set(i, value);
                    return root;
                }
                else
                {
                    throw new NBTTagNotFound(current, t);
                }
            }
            Object t = queue.poll();
            if (current instanceof NBTTagCompound && t instanceof String)
            {
                NBTTagCompound compound = (NBTTagCompound) current;
                String string = (String) t;
                if (!compound.hasKey(string))
                {
                    Object z = queue.peek();
                    if (z instanceof String)
                    {
                        current = compound.getCompound(string);
                    }
                    else if (z instanceof Integer)
                    {
                        current = compound.getList(string, (Integer) z);
                    }
                }
                else
                {
                    current = compound.get(string);
                }
            }
            else if (current instanceof NBTTagList && t instanceof Integer)
            {
                NBTTagList list = (NBTTagList) current;
                int i = (Integer) t;
                if (i == -1)
                {
                    i = list.size() - 1;
                }
                NBTBase b = null;
                if (!list.isEmpty())
                {
                    b = list.get(i);
                }
                else
                {
                    Object z = queue.peek();
                    if (z instanceof String)
                    {
                        b = new NBTTagCompound();
                    }
                    else if (z instanceof Integer)
                    {
                        b = new NBTTagList();
                    }
                }
                while (list.size() <= i)
                {
                    list.add(b.clone());
                }
                current = list.get(i);
            }
            else
            {
                throw new NBTTagNotFound(current, t);
            }
        }
    }

    private NBTBase call(NBTBase root, NBTBase value) throws NBTTagNotFound, NBTTagUnexpectedType
    {
        return set(root, value);
    }

    @Override
    public String toString()
    {
        String s = "";
        for (Object node : values)
        {
            if (node instanceof Integer)
            {
                s += "[" + node + "]";
            }
            else
            {
                s += "." + node;
            }
        }
        if (s.startsWith("."))
        {
            s = s.substring(1);
        }
        if (s.isEmpty())
        {
            s = ".";
        }
        return s;
    }
}
