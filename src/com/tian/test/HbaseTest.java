package com.tian.test;

import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.Cell;

import com.tian.entity.Patent;

public class HbaseTest {

	private static Configuration conf = null;
	private static HTable table = null;
	static {
		conf = HBaseConfiguration.create();
		conf.set("hbase.zookeeper.quorum", "192.168.33.132");
		conf.set("hbase.zookeeper.property.clientPort", "2181");
	}

	// 创建数据库表
	public static void createTable(String tableName, String[] columnFamilys)
			throws Exception {
		// 新建一个数据库管理员
		HBaseAdmin hAdmin = new HBaseAdmin(conf);

		if (hAdmin.tableExists(tableName)) {
			System.out.println("表已经存在");
			System.exit(0);
		} else {
			// 新建一个 scores 表的描述
			HTableDescriptor tableDesc = new HTableDescriptor(TableName
					.valueOf(tableName));
			// 在描述里添加列族
			for (String columnFamily : columnFamilys) {
				tableDesc.addFamily(new HColumnDescriptor(columnFamily));
			}
			// 根据配置好的描述建表
			hAdmin.createTable(tableDesc);
			System.out.println("创建表成功");
		}
		hAdmin.close();
	}

	// 删除数据库表
	public static void deleteTable(String tableName) throws Exception {
		// 新建一个数据库管理员
		HBaseAdmin hAdmin = new HBaseAdmin(conf);

		if (hAdmin.tableExists(tableName)) {
			// 关闭一个表
			hAdmin.disableTable(tableName);
			// 删除一个表
			hAdmin.deleteTable(tableName);
			System.out.println("删除表成功");
		} else {
			System.out.println("删除的表不存在");
			System.exit(0);
		}
		hAdmin.close();
	}

	// 添加一条数据
	public static void addRow(String tableName, String row,
			String columnFamily, String column, String value) throws Exception {
		HTable table = new HTable(conf, tableName);
		Put put = new Put(Bytes.toBytes(row));
		// 参数出分别：列族、列、值
		put.add(Bytes.toBytes(columnFamily), Bytes.toBytes(column), Bytes
				.toBytes(value));
		table.put(put);
		table.close();
	}

	// 删除一条数据
	public static void delRow(String tableName, String row) throws Exception {
		HTable table = new HTable(conf, tableName);
		Delete del = new Delete(Bytes.toBytes(row));
		table.delete(del);
		table.close();
	}

	// 删除多条数据
	public static void delMultiRows(String tableName, String[] rows)
			throws Exception {
		HTable table = new HTable(conf, tableName);
		List<Delete> list = new ArrayList<Delete>();

		for (String row : rows) {
			Delete del = new Delete(Bytes.toBytes(row));
			list.add(del);
		}

		table.delete(list);
		table.close();
	}

	// get row
	public static Patent getRow(String tableName, String row) throws Exception {

		if (table == null) {
			table = new HTable(conf, tableName);
		}
		Get get = new Get(Bytes.toBytes(row));

		Result result = table.get(get);
		Patent patent = new Patent();
		// 输出结果
		for (Cell rowKV : result.rawCells()) {

			/*
			 * System.out.print("Row Name: " + new
			 * String(CellUtil.cloneRow(rowKV)) + " ");
			 * System.out.print("Timestamp: " + rowKV.getTimestamp() + " ");
			 * System.out.print("column Family: " + new
			 * String(CellUtil.cloneFamily(rowKV)) + " ");
			 * System.out.print("column Name:  " + new
			 * String(CellUtil.cloneQualifier(rowKV)) + " ");
			 * System.out.println("Value: " + new
			 * String(CellUtil.cloneValue(rowKV)) + " ");
			 */
			if (new String(CellUtil.cloneQualifier(rowKV)).equals("zlAbstract")) {
				patent.setZlAbstract(new String(CellUtil.cloneValue(rowKV),
						"UTF-8"));
				continue;
			}
			if (new String(CellUtil.cloneQualifier(rowKV))
					.equals("zlApplicant")) {
				patent.setZlApplicant(new String(CellUtil.cloneValue(rowKV),
						"UTF-8"));
				continue;
			}
			if (new String(CellUtil.cloneQualifier(rowKV)).equals("zlFenlei")) {
				patent.setZlFenlei(new String(CellUtil.cloneValue(rowKV),
						"UTF-8"));
				continue;
			}
			if (new String(CellUtil.cloneQualifier(rowKV)).equals("zlDate")) {
				patent
						.setZlDate(new String(CellUtil.cloneValue(rowKV),
								"UTF-8"));
				continue;
			}
			if (new String(CellUtil.cloneQualifier(rowKV)).equals("zlName")) {
				patent
						.setZlName(new String(CellUtil.cloneValue(rowKV),
								"UTF-8"));
				continue;
			}
			if (new String(CellUtil.cloneQualifier(rowKV)).equals("zlNumber")) {
				patent.setZlNumber(new String(CellUtil.cloneValue(rowKV),
						"UTF-8"));
				continue;
			}
			if (new String(CellUtil.cloneQualifier(rowKV)).equals("zlUrl")) {
				patent
						.setZlUrl(new String(CellUtil.cloneValue(rowKV),
								"UTF-8"));
				continue;
			}
		}
		// table.close();
		return patent;
	}

	// get row
	public static List<Patent> getByRows(String tableName, List<Long> pids)
			throws Exception {
		List<Patent> patents = new ArrayList<Patent>(); 
		if (table == null) {
			table = new HTable(conf, tableName);
		}
		for (long pid : pids) {
			String row = String.valueOf(pid);
			Get get = new Get(Bytes.toBytes(row));

			Result result = table.get(get);
			Patent patent = new Patent();
			// 输出结果
			for (Cell rowKV : result.rawCells()) {

				/*
				 * System.out.print("Row Name: " + new
				 * String(CellUtil.cloneRow(rowKV)) + " ");
				 * System.out.print("Timestamp: " + rowKV.getTimestamp() + " ");
				 * System.out.print("column Family: " + new
				 * String(CellUtil.cloneFamily(rowKV)) + " ");
				 * System.out.print("column Name:  " + new
				 * String(CellUtil.cloneQualifier(rowKV)) + " ");
				 * System.out.println("Value: " + new
				 * String(CellUtil.cloneValue(rowKV)) + " ");
				 */
				if (new String(CellUtil.cloneQualifier(rowKV))
						.equals("zlAbstract")) {
					patent.setZlAbstract(new String(CellUtil.cloneValue(rowKV),
							"UTF-8"));
					continue;
				}
				if (new String(CellUtil.cloneQualifier(rowKV))
						.equals("zlApplicant")) {
					patent.setZlApplicant(new String(
							CellUtil.cloneValue(rowKV), "UTF-8"));
					continue;
				}
				if (new String(CellUtil.cloneQualifier(rowKV))
						.equals("zlFenlei")) {
					patent.setZlFenlei(new String(CellUtil.cloneValue(rowKV),
							"UTF-8"));
					continue;
				}
				if (new String(CellUtil.cloneQualifier(rowKV)).equals("zlDate")) {
					patent.setZlDate(new String(CellUtil.cloneValue(rowKV),
							"UTF-8"));
					continue;
				}
				if (new String(CellUtil.cloneQualifier(rowKV)).equals("zlName")) {
					patent.setZlName(new String(CellUtil.cloneValue(rowKV),
							"UTF-8"));
					continue;
				}
				if (new String(CellUtil.cloneQualifier(rowKV))
						.equals("zlNumber")) {
					patent.setZlNumber(new String(CellUtil.cloneValue(rowKV),
							"UTF-8"));
					continue;
				}
				if (new String(CellUtil.cloneQualifier(rowKV)).equals("zlUrl")) {
					patent.setZlUrl(new String(CellUtil.cloneValue(rowKV),
							"UTF-8"));
					continue;
				}
			}
			patents.add(patent);
			// table.close();
			
		}
		return patents;
	}

	// get all records
	public static void getAllRows(String tableName) throws Exception {
		HTable table = new HTable(conf, tableName);
		Scan scan = new Scan();
		ResultScanner results = table.getScanner(scan);
		// 输出结果
		for (Result result : results) {
			for (Cell rowKV : result.rawCells()) {
				System.out.print("Row Name: "
						+ new String(CellUtil.cloneRow(rowKV)) + " ");
				System.out.print("Timestamp: " + rowKV.getTimestamp() + " ");
				System.out.print("column Family: "
						+ new String(CellUtil.cloneFamily(rowKV)) + " ");
				System.out.print("column Name:  "
						+ new String(CellUtil.cloneQualifier(rowKV)) + " ");
				System.out.println("Value: "
						+ new String(CellUtil.cloneValue(rowKV)) + " ");
			}
		}
		table.close();
	}

	// main
	public static void main(String[] args) {
		try {
			/*
			 * String tableName = "users2";
			 * 
			 * 
			 * // 第一步：创建数据库表：“users2” String[] columnFamilys = { "info",
			 * "course" }; HbaseTest.createTable(tableName, columnFamilys);
			 * 
			 * 
			 * // 第二步：向数据表的添加数据 // 添加第一行数据 HbaseTest.addRow(tableName, "tht",
			 * "info", "age", "20"); HbaseTest.addRow(tableName, "tht", "info",
			 * "sex", "boy"); HbaseTest.addRow(tableName, "tht", "course",
			 * "china", "97"); HbaseTest.addRow(tableName, "tht", "course",
			 * "math", "128"); HbaseTest.addRow(tableName, "tht", "course",
			 * "english", "85"); // 添加第二行数据 HbaseTest.addRow(tableName,
			 * "xiaoxue", "info", "age", "19"); HbaseTest.addRow(tableName,
			 * "xiaoxue", "info", "sex", "boy"); HbaseTest.addRow(tableName,
			 * "xiaoxue", "course", "china", "90"); HbaseTest.addRow(tableName,
			 * "xiaoxue", "course", "math", "120"); HbaseTest .addRow(tableName,
			 * "xiaoxue", "course", "english", "90"); // 添加第三行数据
			 * HbaseTest.addRow(tableName, "qingqing", "info", "age", "18");
			 * HbaseTest.addRow(tableName, "qingqing", "info", "sex", "girl");
			 * HbaseTest .addRow(tableName, "qingqing", "course", "china",
			 * "100"); HbaseTest.addRow(tableName, "qingqing", "course", "math",
			 * "100"); HbaseTest.addRow(tableName, "qingqing", "course",
			 * "english", "99"); // 第三步：获取一条数据 System.out.println("获取一条数据");
			 * HbaseTest.getRow(tableName, "tht"); // 第四步：获取所有数据
			 * System.out.println("获取所有数据"); HbaseTest.getAllRows(tableName); //
			 * 第五步：删除一条数据 System.out.println("删除一条数据");
			 * HbaseTest.delRow(tableName, "tht");
			 * HbaseTest.getAllRows(tableName); // 第六步：删除多条数据
			 * System.out.println("删除多条数据"); String[] rows = { "xiaoxue",
			 * "qingqing" }; HbaseTest.delMultiRows(tableName, rows);
			 * HbaseTest.getAllRows(tableName); // 第八步：删除数据库
			 * System.out.println("删除数据库"); HbaseTest.deleteTable(tableName);
			 */
			String tableName = "hpatents";
			HbaseTest.getRow(tableName, "803810");

		} catch (Exception err) {
			err.printStackTrace();
		}
	}
}