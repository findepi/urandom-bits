package findepi.random;

public class H {
	static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
	
	public static void main(String[] args) {
		System.out.println("IdProviderRange".hashCode());
		System.out.println(56332369);
		System.out.println(hash(56332369));
		System.out.println((57190603) & (255));
	}
}
