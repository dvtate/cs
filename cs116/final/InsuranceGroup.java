
import java.util.*;
import java.io.*;

public class InsuranceGroup {
    protected List<Insurance> policies;
    private static int uid = 1000;
    protected int id;

    public InsuranceGroup() {
        this.id = uid++;
        this.policies = new ArrayList<Insurance>();
    }

    // extra stuff to gaurantee 4 digit id vlaue
    public String getGroupID() {
        String suffix = "" + this.id;
        while (suffix.length() < 4)
            suffix = "0" + suffix;
        return "GR-" + suffix;
    }


    private double avgAge() {
        double total = 0;
        for (Insurance p : this.policies)
            total += p.getAge();
        return total / (double) this.policies.size();
    }
    private int totalMonthlyFee() {
        int ret = 0;
        for (Insurance p : this.policies)
            ret += p.getCost();
        return ret;
    }

    public boolean canAdd() { return this.policies.size() < 5 || this.avgAge() < 45; }
    public void addPolicy(Insurance policyObject) {
        if (this.canAdd())
            this.policies.add(policyObject);
    }
    public String toString() {
        return "===================================="
            + "\nInsurance Group ID: " + this.getGroupID()
            + "\nNo. of policies: " + this.policies.size()
            + "\nTotal monthly fee: " + this.totalMonthlyFee()
            + "\nAverage age: " + this.avgAge()
            + "\n===============================";
    }
};
