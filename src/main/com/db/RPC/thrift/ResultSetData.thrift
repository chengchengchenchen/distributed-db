/**
 * 返回的数据表结构，参考mysql api
 */
struct ResultSetData {
  1: list<string> columnNames; // 列名列表
  2: list<list<string>> rows; // 行数据列表
}

