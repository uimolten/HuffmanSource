
import java.util.ArrayList;

public class HuffPriorityQ {

    private ArrayList<TreeNode> con;
    private int size;

    public HuffPriorityQ() {
        con = new ArrayList<>();
        size = 0;
    }

    public boolean enqueue(TreeNode n) {
        if (size == 0) {
            con.add(n);
        }

        for (int i = 0; i < con.size(); i++) {
            int compare = n.compareTo(con.get(i));


          if (compare < 0) {
                con.add(i, n);
                size++;
                return true;
            }
        }
        return false;
    }
    public TreeNode dequeue() {
        if (size > 0) {
            size--;
            return con.remove(0);
        }
        return null;
    }

    public int size() {
        return size;
    }

    public String toString() {
        StringBuilder result = new StringBuilder("(");
        for (TreeNode n : con) {
            result.append(n);
            result.append(", ");
        }
        result.append(")");
        return result.toString();

    }

}
