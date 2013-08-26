import java.net.ServerSocket;

/**
 * @author piofin <piotr.findeisen@syncron.com>
 * @since Nov 20, 2012
 */
public class Acc {
	public static void main(String[] args) throws Exception {
		Thread t = new Thread(new Runnable() {
			public void run() {
				
				try {
					ServerSocket s = new ServerSocket(0);

					System.out.println("accept()");
					s.accept();
					
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		t.setDaemon(true);
		t.start();
		
		Thread.sleep(100);
		
		System.out.println("checking");
		
		System.out.println("state: " + t.getState());
	}
}
