/**
 * 文件数据结构，binary生成byte[]，读写时转换为bytebuffer
 */
struct FileData {
  1: string fileName;
  2: binary fileContent;
}
