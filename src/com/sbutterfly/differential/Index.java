package com.sbutterfly.differential;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public enum Index {
    First,
    Second,
    Third,
    Fourth,
    Fifth,
    Sixth,
    Seventh,
    Eighth,
    Nineth,
    Tenth,
    Time;

    public static int toInt(Index index){
        if (index == Index.Time) return -1;

        if (index == Index.First) return 0;
        if (index == Index.Second) return 1;
        if (index == Index.Third) return 2;
        if (index == Index.Fourth) return 3;
        if (index == Index.Fifth) return 4;
        if (index == Index.Sixth) return 5;
        if (index == Index.Seventh) return 6;
        if (index == Index.Eighth) return 7;
        if (index == Index.Nineth) return 8;
        if (index == Index.Tenth) return 9;

        throw new NotImplementedException();
    }

    public static Index toIndex(int index){
        if (index == -1) return Index.Time;

        if (index == 0) return Index.First;
        if (index == 1) return Index.Second;
        if (index == 2) return Index.Third;
        if (index == 3) return Index.Fourth;
        if (index == 4) return Index.Fifth;
        if (index == 5) return Index.Sixth;
        if (index == 6) return Index.Seventh;
        if (index == 7) return Index.Eighth;
        if (index == 8) return Index.Nineth;
        if (index == 9) return Index.Tenth;

        throw new NotImplementedException();
    }
}
