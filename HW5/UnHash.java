public class UnHash {
    public static void main(String[] args){
        String to_unhash = args[0];
        UnHash crack = new UnHash();
        System.out.println(crack.unhash(to_unhash));
    }

    public int unhash(String to_unhash){
        int val = 0;
        Hash hash = new Hash();

        while (true){
            if (hash.hash(val).equals(to_unhash)){
                return val;
            }
            val++;
        }
    }
}
