import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

import static org.junit.Assert.*;

@Ignore
public class RecursionTest {
    private final static int N = 3000;

    private static class TreeNode{
        int n;
        TreeNode l; TreeNode r;

        public TreeNode(int n, TreeNode l, TreeNode r) {
            this.n = n;
            this.l = l;
            this.r = r;
        }
    }

    private int recSum(TreeNode root){
        if(root == null) return 0;
        return root.n + recSum(root.l) + recSum(root.r);
    }

    private static class SumCounter extends RecursiveTask<Integer>{
        private final TreeNode node;

        private SumCounter(TreeNode node) {
            this.node = node;
        }

        @Override
        protected Integer compute() {
            if(node == null) return 0;
            int sum = node.n;
            SumCounter leftTask = new SumCounter(node.l);
            SumCounter rightTask = new SumCounter(node.r);
            leftTask.fork();
            rightTask.fork();
            sum += leftTask.join();
            sum += rightTask.join();
            return sum;
        }
    }

    private int fjpSum(TreeNode root) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        return forkJoinPool.submit(new SumCounter(tree)).get();
    }

    private TreeNode tree;
    @Before
    public void init(){
        tree = new TreeNode(0, null, null);
        TreeNode last = tree;
        for(int i = 0; i < N; i++){
            last.l = new TreeNode(1,null,null);
            TreeNode newNode = new TreeNode(1, null, null);
            last.r = newNode;
            last = newNode;
        }
    }

    @Test(/*expected = StackOverflowError.class*/)
    public void recursionTest(){
        assertEquals(N*2,recSum(tree));
    }

    @Test
    public void forkJoinPoolTest() throws ExecutionException, InterruptedException {
        assertEquals(N*2, fjpSum(tree));
    }
}
