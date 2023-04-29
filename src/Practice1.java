import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.*;

public class Practice1 {
    public static HashMap<String,ArrayList<String>> dataField=new HashMap<>(){{
        put("t_id",new ArrayList<String>());
        put("c_id",new ArrayList<String>());
        put("b_id",new ArrayList<String>());
        put("used",new ArrayList<String>(Arrays.asList("grocery", "beauty", "ATM", "clothes","culture","edu","medi","traffic")));
        put("is_dep",new ArrayList<String>(Arrays.asList("deposit","withdraw")));
        put("time", new ArrayList<String>(Arrays.asList("1","2","3","4","5","6","7","8","9","10","11","12")));
    }};
    public static void main(String[] args){
        Connection conn=null;
        String dbURL="jdbc:mysql://localhost:3306/db_practice?serverTimezone=Asia/Seoul&useSSL=false&useUnicode=true&characterEncoding=utf8";
        PreparedStatement pstmt;
        ResultSet rs;

        String dbID="root";
        String dbPassword="1234";
        Scanner sc=new Scanner(System.in);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn=DriverManager.getConnection(dbURL,dbID,dbPassword);
        }catch (Exception e){
            e.printStackTrace();
        }
        while (true){
            System.out.println("Enter the number of menu");
            System.out.println("(1)create table (2) insert data (3) create index (4) result query (5) end");
            int menu=sc.nextInt();
            if (menu==1) create_table();
            else if (menu==2) insert_data();
            else if (menu==3) create_index();
            else if (menu==4) result_query();
            else break;
        }

    }
    public static void create_table(){
        Connection conn=null;
        String dbURL="jdbc:mysql://localhost:3306/db_practice?serverTimezone=Asia/Seoul&useSSL=false&useUnicode=true&characterEncoding=utf8";
        PreparedStatement pstmt;
        ResultSet rs;

        String dbID="root";
        String dbPassword="1234";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn=DriverManager.getConnection(dbURL,dbID,dbPassword);
        }catch (Exception e){
            e.printStackTrace();
        }
        Scanner sc=new Scanner(System.in);
        String sql="CREATE TABLE ";
        System.out.print("Enter Table Name: ");
        String title=sc.nextLine();
        // primary key는 idx로 자동 할당
        sql+=title+" ( idx integer auto_increment, ";
        System.out.println("Enter attribute name, attribute constraint");
        System.out.println("If you want to finish, enter \'end\'");
        while (true){
            String attr_sql=sc.nextLine();
            if (attr_sql.equals("end")){
                System.out.print("Enter PK: ");
                String pk=sc.nextLine();
                sql+=", primary key(idx,"+pk+"))";
                break;
            }
            sql+=attr_sql;
        }
        try{
            Statement stmt=conn.createStatement();
            System.out.println(sql);
            boolean b=stmt.execute(sql);
            System.out.println(b);
            boolean c=stmt.execute("ALTER TABLE "+title+" AUTO_INCREMENT=0");
            System.out.println(c);
        }catch (Exception e){
            System.out.println(e);
        }
        sc.close();
    }

    public static void insert_data(){
        Connection conn=null;
        String dbURL="jdbc:mysql://localhost:3306/db_practice?serverTimezone=Asia/Seoul&useSSL=false&useUnicode=true&characterEncoding=utf8";
        PreparedStatement pstmt;
        ResultSet rs;

        String dbID="root";
        String dbPassword="1234";

        Map<Integer,ArrayList> hm=new HashMap<>();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn=DriverManager.getConnection(dbURL,dbID,dbPassword);
            Statement stmt=conn.createStatement();

            Scanner sc=new Scanner(System.in);
            System.out.print("Enter table to input data: ");
            String name=sc.nextLine();

            rs=stmt.executeQuery("select * from "+name);
            ResultSetMetaData rsMetaData=rs.getMetaData();

            ArrayList columns=new ArrayList();
            int count=rsMetaData.getColumnCount();
            for (int i=2;i<=count;i++){
                String column_name=rsMetaData.getColumnName(i);
                System.out.println(column_name);
                columns.add(column_name);
                System.out.print("Input data type you want: ");
                String dataInput=sc.nextLine();
                if (dataInput.length()>0){
                    ArrayList<String> dataset=new ArrayList<>(Arrays.asList(dataInput.split(" ")));
                    hm.put(i-1,dataset);
                    //dataField.put(column_name,dataset);
                }else {
                    hm.put(i-1, new ArrayList<>());
                    //dataField.put(column_name,new ArrayList());
                }
            }

            for (int j=0;j<100;j++){
                ArrayList<String> row_data=new ArrayList<>();
                for (int key:hm.keySet()){
                    ArrayList hm_result=hm.get(key);
                    if (hm_result.size()>0){
                        int randomNumber=(int)(Math.random()*(hm_result.size()));
                        row_data.add("\'"+hm_result.get(randomNumber).toString()+"\'");
                    }else{
                        String randomString="";
                        for (int i=0;i<10;i++){
                            char ch=(char)((int)(Math.random()*25)+97);
                            randomString+=ch;
                        }
                        row_data.add("\'"+randomString+"\'");
                    }
                }
                String insert_sql="INSERT INTO "+name+"("+String.join(",",columns)+") values("+String.join(",",row_data)+")";
                int res_count=stmt.executeUpdate(insert_sql);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void create_index(){
        Connection conn=null;
        String dbURL="jdbc:mysql://localhost:3306/db_practice?serverTimezone=Asia/Seoul&useSSL=false&useUnicode=true&characterEncoding=utf8";
        PreparedStatement pstmt;
        ResultSet rs;
        Scanner sc=new Scanner(System.in);

        String dbID="root";
        String dbPassword="1234";
        String title;

        ArrayList<String> index_field=null;
        System.out.print("Enter table name:");
        title=sc.nextLine();
        System.out.print("Enter make index you want: ");

        String index_line=sc.nextLine();
        index_field=new ArrayList<>(Arrays.asList(index_line.split(" ")));

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
            Statement stmt = conn.createStatement();

            for (String key:index_field){
                ArrayList<String> values=dataField.get(key);
                //char [] buf=new char[1024];
                for (int i=0;i<values.size();i++){
                    String result_string="";
                    rs=stmt.executeQuery("select "+key+" from "+title);
                    FileOutputStream fos=new FileOutputStream("D:/데이터베이스시스템/index_"+key+"_"+values.get(i)+".txt");
                    BufferedOutputStream bos=new BufferedOutputStream(fos,100);
                    // File 생성, write append 가능하게 name: index_key_values.get(i)
                    while (rs.next()){
                        if (rs.getString(key).equals(values.get(i))){
                            result_string+="1";
                        }else{
                            result_string+="0";
                        }
                    }
                    bos.write(result_string.getBytes());
                }

            }
        }catch (Exception e){
            System.out.println(e);
        }


    }

    public static void result_query(){
        Connection conn=null;
        String dbURL="jdbc:mysql://localhost:3306/db_practice?serverTimezone=Asia/Seoul&useSSL=false&useUnicode=true&characterEncoding=utf8";
        PreparedStatement pstmt;
        ResultSet rs;
        Scanner sc=new Scanner(System.in);

        String dbID="root";
        String dbPassword="1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
            Statement stmt = conn.createStatement();

            ArrayList<String> to_search=new ArrayList<>();
            List operator=null;
            // 값 입력 time 7 / used grocery / end
            // operator 입력 ( and / or )
            System.out.println("Enter value to search");
            System.out.print("used>> ");
            String used_value=sc.nextLine();

            System.out.print("is_dep>> ");
            String dep_value=sc.nextLine();

            System.out.print("time>> ");
            String time_value=sc.nextLine();

            System.out.print("Enter operator(and/or): ");
            String operLine=sc.nextLine();
            operator=Arrays.asList(operLine.split(" "));

            FileInputStream used_fis=new FileInputStream("D:/데이터베이스시스템/index_used_"+used_value+".txt");
            FileInputStream dep_fis=new FileInputStream("D:/데이터베이스시스템/index_is_dep_"+dep_value+".txt");
            FileInputStream time_fis=new FileInputStream("D:/데이터베이스시스템/index_time_"+time_value+".txt");

            byte[] used_buff=new byte[50];
            byte[] dep_buff=new byte[50];
            byte[] time_buff=new byte[50];

            int idx=1;
            System.out.println("==========================================RESULT=============================================");
            System.out.println("|     t_id     |     c_id     |     b_id     |     used     |     is_dep     |     time     |");
            while (used_fis.read(used_buff)!=-1){
                dep_fis.read(dep_buff);
                time_fis.read(time_buff);

                String used_strBuff=new String(used_buff);
                String dep_strBuff=new String(dep_buff);
                String time_strBuff=new String(time_buff);

                String result="";
                if (operator.get(0).equals("and")){
                    result=and_operator(used_strBuff,dep_strBuff);
                }else if (operator.get(0).equals("or")){
                    result=or_operator(used_strBuff,dep_strBuff);
                }
                if (operator.get(1).equals("and")){
                    result=and_operator(result,time_strBuff);
                }else if (operator.get(1).equals("or")){
                    result=or_operator(result,time_strBuff);
                }
                for (int i=0;i<result.length();i++){
                    if (result.charAt(i)=='1'){
                        rs=stmt.executeQuery("select * from banklog where idx="+Integer.toString(idx));
                        while (rs.next()){
                            System.out.print((String.format("%14s",rs.getString("t_id"))));
                            System.out.print(String.format("%14s",rs.getString("c_id")));
                            System.out.print(String.format("%14s",rs.getString("b_id")));
                            System.out.print(String.format("%15s",rs.getString("used")));
                            System.out.print(String.format("%15s",rs.getString("is_dep")));
                            System.out.println(String.format("%18s",rs.getString("time")));
                        }
                    }
                    idx+=1;
                }
            }

            used_fis.close();
            dep_fis.close();
            time_fis.close();

        }catch (Exception e){
            System.out.println(e);
        }

        }

        public static String and_operator(String op1,String op2){
            String result="";
            for (int i=0;i<op1.length();i++){
                if (op1.charAt(i)=='1' && op2.charAt(i)=='1'){
                    result+='1';
                }else{
                    result+='0';
                }
            }
            return result;
        }
        public static String or_operator(String op1,String op2){
            String result="";
            for (int i=0;i<op1.length();i++){
                if (op1.charAt(i)=='1'||op2.charAt(i)=='1'){
                    result+='1';
                }else{
                    result+='0';
                }
            }
            return result;
        }

}
