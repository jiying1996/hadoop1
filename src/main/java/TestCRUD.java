import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class TestCRUD {
    private static final String connectString = "mini1:2181,"
            + "mini2:2181," + "mini3:2181";
    private static final int sessionTimeout = 2000;

    public static void main(String[] args) throws Exception{
//        testConnect();
//        testCreate();
//        testDelete();
//        testIsExist();
        getData();
    }

    //测试连接
    public static void testConnect() {
        ZooKeeper zkClient = null;
        try {
            zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    System.out.println("====" + watchedEvent.getType() + "===" + watchedEvent.getState());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("-----------------" + zkClient.getState());
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("=====---====" + zkClient.getState());
        }
    }
    
    //测试创建
    public static void testCreate() throws Exception{
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("======"+watchedEvent.getType());
            }
        });
        Thread.sleep(10*1000);
        if(zkClient.getState() == ZooKeeper.States.CONNECTED){
            String nodeCreated = zkClient.create("/age2",   
                    "18".getBytes(),                    //节点中的数据
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,       //节点的权限
                    CreateMode.PERSISTENT);             //节点的类型
            System.out.println("创建成功后，节点的真实路径是："+nodeCreated);
        }
    }
    //测试删除
    public static void testDelete() throws Exception{
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("======"+watchedEvent.getType());
            }
        });
        Thread.sleep(10*1000);
        zkClient.delete("/age2",-1); //-1表示所有版本
        System.out.println("删除成功。");
    }
    
    //测试是否存在
    public static void testIsExist() throws Exception{
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("======"+watchedEvent.getType());
            }
        });
        Thread.sleep(10*1000);
        Stat stat = zkClient.exists("/age",true);
        System.out.println(stat==null?"不存在该节点":"存在该节点");
        //第二个参数表示  是否监听”/age“节点的状态的改变，为true时，则监听，当另一个zookeeper客户端修改、删除该节点的值时，回调watcher
    }

    //获取znode的数据
    public static void getData() throws Exception{
        ZooKeeper zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println("======"+watchedEvent.getType());
            }
        });
        
        Thread.sleep(10*1000);
        byte[] bytes = zkClient.getData("/age",false,null);
        System.out.println("节点的数据为："+ new String(bytes));
    }
    
    //获取子节点
}
