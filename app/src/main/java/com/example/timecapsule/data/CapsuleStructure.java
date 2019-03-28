package com.example.timecapsule.data;

import java.util.List;
import java.util.Map;

public class CapsuleStructure {
    public String capsulename;
    public byte[] encryptedFileBytes;
    public List<User> totalUsers;
    public int totalNum;
    public int leastNum;
    public int MB;
    public byte[] salt;
    public Map<int[], byte[]> subsets;
    public String date;

}
