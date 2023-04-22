import java.sql.*;
import java.util.*;

public class Practice1 {
    public static void main(String[] args){
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
        //create_table();
        insert_data();
        //String SQL = "CREATE TABLE MEMO_TABLE ( name varchar(4) primary key, msg varchar(100))";
        try {
            //Statement stmt=conn.createStatement();
            //boolean b=stmt.execute(SQL);

        }catch (Exception e){
            e.printStackTrace();
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
        System.out.println(sql);
        try{
            Statement stmt=conn.createStatement();
            boolean b=stmt.execute(sql);
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
                }else {
                    hm.put(i-1, new ArrayList());
                }
            }

            for (int j=0;j<10;j++){
                ArrayList<String> row_data=new ArrayList<>();
                for (int key:hm.keySet()){
                    ArrayList hm_result=hm.get(key);
                    if (hm_result.size()>0){
                        int randomNumber=(int)(Math.random()*(hm_result.size()));
                        row_data.add("\'"+hm_result.get(randomNumber).toString()+"\'");
                    }else{
                        String randomString="";
                        for (int i=0;i<3;i++){
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
        // 저장할 때 3bit씩 저장(한자리수 integer만 저장되도록)
    }

    public static void result_query(){
        //Integer.toBinaryString
    }
}
