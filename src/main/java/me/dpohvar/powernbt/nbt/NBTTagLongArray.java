package me.dpohvar.powernbt.nbt;

import java.util.*;

import static me.dpohvar.powernbt.utils.NBTUtils.nbtUtils;

public class NBTTagLongArray extends NBTTagNumericArray<Long>
{

    public static final byte typeId = 12;

    public NBTTagLongArray()
    {
        super(new long[0]);
    }

    public NBTTagLongArray(String ignored)
    {
        super(new long[0]);
    }

    public NBTTagLongArray(long[] l)
    {
        super(nbtUtils.createTag(l, typeId));
    }

    public NBTTagLongArray(String ignored, long[] l)
    {
        this(l);
    }

    public NBTTagLongArray(boolean ignored, Object tag)
    {
        super(tag);
        if (nbtUtils.getTagType(handle) != typeId)
        {
            throw new IllegalArgumentException();
        }
    }

    public static ArrayList<Long> longArrToList(long[] ln)
    {
        ArrayList<Long> temp = new ArrayList<Long>(ln.length);
        for (long anLn : ln)
        {
            temp.add(anLn);
        }
        return temp;
    }

    public static long[] listToLongArr(Collection<Long> ln)
    {
        long[] temp = new long[ln.size()];
        int i = 0;
        for (Long anLn : ln)
        {
            temp[i++] = anLn;
        }
        return temp;
    }

    public boolean equals(Object o)
    {
        if (o instanceof NBTBase)
        {
            ((NBTBase) o).getHandle();
        }
        return handle.equals(o);
    }

    public int hashCode()
    {
        return handle.hashCode();
    }

    public long[] get()
    {
        return (long[]) super.get();
    }

    public void set(long[] value)
    {
        super.set(value);
    }

    public int size()
    {
        return get().length;
    }

    @Override
    public boolean contains(Object o)
    {
        return longArrToList(get()).contains(o);
    }

    public Long get(int i)
    {
        long[] array = get();
        if (i >= array.length) return null;
        return array[i];
    }

    public Long set(int i, Number value)
    {
        Long res = get(i);
        long[] array = get();
        List<Long> list = new LinkedList<Long>();
        for (long b : array) list.add(b);
        while (list.size() <= i)
        {
            list.add(0L);
        }
        list.set(i, value.longValue());
        long[] result = new long[list.size()];
        int t = 0;
        for (long b : list) result[t++] = b;
        set(result);
        return res;
    }

    public Long remove(int i)
    {
        Long res = get(i);
        long[] array = get();
        if (i < 0 || i >= array.length) return res;
        List<Long> list = new LinkedList<Long>();
        for (long b : array) list.add(b);
        while (list.size() <= i)
        {
            list.add((long) 0);
        }
        list.remove(i);
        long[] result = new long[list.size()];
        int t = 0;
        for (long b : list) result[t++] = b;
        set(result);
        return res;
    }

    @Override
    public int indexOf(Object o)
    {
        return longArrToList(get()).indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o)
    {
        return longArrToList(get()).lastIndexOf(o);
    }

    @Override
    public List<Long> subList(int fromIndex, int toIndex)
    {
        long[] r = new long[toIndex - fromIndex];
        int t = 0;
        long[] s = get();
        for (int i = fromIndex; i < toIndex; i++)
        {
            r[t++] = s[i];
        }
        return new NBTTagLongArray(r);
    }

    @Override
    public boolean add(Number value)
    {
        long[] array = get();
        List<Long> list = new LinkedList<Long>();
        for (long b : array) list.add(b);
        list.add(value.longValue());
        long[] result = new long[list.size()];
        int t = 0;
        for (long b : list) result[t++] = b;
        set(result);
        return false;
    }

    @Override
    public boolean remove(Object o)
    {
        List<Long> longs = longArrToList(get());
        boolean result = longs.remove(o);
        if (result)
        {
            set(listToLongArr(longs));
        }
        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        List<Long> longs = longArrToList(get());
        boolean result = longs.removeAll(c);
        if (result)
        {
            set(listToLongArr(longs));
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        List<Long> longs = longArrToList(get());
        boolean result = longs.retainAll(c);
        if (result)
        {
            set(listToLongArr(longs));
        }
        return result;
    }

    @Override
    public void clear()
    {
        set(new long[0]);
    }

    @Override
    public String toString()
    {
        return Arrays.toString(get());
    }

    @Override
    public byte getTypeId()
    {
        return 12;
    }
}
