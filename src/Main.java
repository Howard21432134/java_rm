import java.io.*;
import java.util.*;
import java.io.FileNotFoundException;

public  class Main {
    static Vector<String> wordlist = new Vector<String>();

    public static void absorb_words(String strs){
        int ll = strs.length();
        String buff = "";
        for(int i = 0;i<ll;i++){
            if(!(strs.charAt(i)>= 'A' && strs.charAt(i) <= 'Z' || strs.charAt(i)>= 'a' && strs.charAt(i) <= 'z')){
                if(buff != ""){
                    wordlist.add(buff);
                    buff = "";
                }
                else continue;
            }
            else buff = buff + strs.charAt(i);
        }
    }

    public static void word_vec(String fileName){
        File file = new File(fileName);
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while((tempString = reader.readLine())!=null){
                absorb_words(tempString);
                line++;
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e1){
                }
            }
        }
    }

    static Map<Queue<String>,Vector<String>> word_map = new HashMap<Queue<String>,Vector<String>>();
    static void mmap(int n){
        Queue<String> q = new LinkedList<String>();
        for(int i = 0;i<n-1;i++){
            q.offer(wordlist.get(i));
        }
        for(int j = n-1;j < wordlist.size();j++){
            Vector<String> v1 =  new Vector<String>();
            v1 = word_map.get(q);
            String s1 = wordlist.get(j);
            v1.addElement(s1);
            word_map.put(q,v1);
            q.poll();
            q.offer(wordlist.get(j));
        }
    }

    static Vector<String> new_text;
    public static void find_first_word(int n){
        int number = 0;
        while(number > wordlist.size() || number <= 0){
            Random rand = new Random();
            number = rand.nextInt(wordlist.size()) -1;
        }
        String first_word = wordlist.get(number);
        new_text.add(first_word);
        if(wordlist.size() - number < n){
            System.out.println("Invalid number of text! Please retry!");
            Scanner s = new Scanner(System.in);
            n = s.nextInt();
        }
        else{
            for(int i = 0;i<n-1;i++){
                new_text.add(wordlist.get(number + i + 1));
            }
        }
    }

    static void random_writing(int qty){
        System.out.println("Please input the length of the text:");
        Scanner s = new Scanner(System.in);
        int mn = s.nextInt();
        mmap(mn);
        find_first_word(mn);
        Queue<String> newq = new LinkedList<String>();
        for(int w = 0; w < new_text.size();w++){
            newq.add(new_text.get(w));
        }
        while(new_text.size() < qty){
            int number;
            Random rand = new Random();
            number = rand.nextInt(word_map.get(newq).size()) -1;
            String nword = word_map.get(newq).get(number);
            new_text.add(nword);
            newq.poll();
            newq.offer(nword);

        }
    }

    public static void main(String[] args){
        System.out.println("Please input the filename:");
        String filename;
        Scanner s = new Scanner(System.in);
        filename = s.nextLine();
        word_vec(filename);
        int n = 0;
        while(n == 0){
            System.out.println("Please input N or input 0 to quit: ");
            int qty;
            qty = s.nextInt();
            if(qty < 0){
                System.out.println("Invalid length!Please input qty > 0!");
                continue;
            }
            if(qty == 0) n++;
            else{
                random_writing(qty);
                for(int k = 0;k < new_text.size();k++){
                    System.out.println(new_text.get(k)+"");
                }
                System.out.println('\n');
                System.out.println(new_text.size());
                new_text.clear();
            }
        }
        return;
    }

}
