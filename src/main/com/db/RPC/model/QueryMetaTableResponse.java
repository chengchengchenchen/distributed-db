package com.db.RPC.model; /**
 * Autogenerated by Thrift Compiler (0.18.1)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
/**
 * 响应体：Master返回tableMeta所在region
 */
@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
@javax.annotation.Generated(value = "Autogenerated by Thrift Compiler (0.18.1)", date = "2023-05-25")
public class QueryMetaTableResponse implements org.apache.thrift.TBase<QueryMetaTableResponse, QueryMetaTableResponse._Fields>, java.io.Serializable, Cloneable, Comparable<QueryMetaTableResponse> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("com.db.RPC.model.QueryMetaTableResponse");

  private static final org.apache.thrift.protocol.TField LOCATED_SERVER_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("locatedServerName", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField LOCATED_SERVER_URL_FIELD_DESC = new org.apache.thrift.protocol.TField("locatedServerUrl", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField BASE_RESP_FIELD_DESC = new org.apache.thrift.protocol.TField("baseResp", org.apache.thrift.protocol.TType.STRUCT, (short)255);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new QueryMetaTableResponseStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new QueryMetaTableResponseTupleSchemeFactory();

  public @org.apache.thrift.annotation.Nullable java.lang.String locatedServerName; // required
  public @org.apache.thrift.annotation.Nullable java.lang.String locatedServerUrl; // required
  public @org.apache.thrift.annotation.Nullable BaseResp baseResp; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LOCATED_SERVER_NAME((short)1, "locatedServerName"),
    LOCATED_SERVER_URL((short)2, "locatedServerUrl"),
    BASE_RESP((short)255, "baseResp");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // LOCATED_SERVER_NAME
          return LOCATED_SERVER_NAME;
        case 2: // LOCATED_SERVER_URL
          return LOCATED_SERVER_URL;
        case 255: // BASE_RESP
          return BASE_RESP;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    @Override
    public short getThriftFieldId() {
      return _thriftId;
    }

    @Override
    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LOCATED_SERVER_NAME, new org.apache.thrift.meta_data.FieldMetaData("locatedServerName", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.LOCATED_SERVER_URL, new org.apache.thrift.meta_data.FieldMetaData("locatedServerUrl", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.BASE_RESP, new org.apache.thrift.meta_data.FieldMetaData("baseResp", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, BaseResp.class)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(QueryMetaTableResponse.class, metaDataMap);
  }

  public QueryMetaTableResponse() {
  }

  public QueryMetaTableResponse(
    java.lang.String locatedServerName,
    java.lang.String locatedServerUrl,
    BaseResp baseResp)
  {
    this();
    this.locatedServerName = locatedServerName;
    this.locatedServerUrl = locatedServerUrl;
    this.baseResp = baseResp;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public QueryMetaTableResponse(QueryMetaTableResponse other) {
    if (other.isSetLocatedServerName()) {
      this.locatedServerName = other.locatedServerName;
    }
    if (other.isSetLocatedServerUrl()) {
      this.locatedServerUrl = other.locatedServerUrl;
    }
    if (other.isSetBaseResp()) {
      this.baseResp = new BaseResp(other.baseResp);
    }
  }

  @Override
  public QueryMetaTableResponse deepCopy() {
    return new QueryMetaTableResponse(this);
  }

  @Override
  public void clear() {
    this.locatedServerName = null;
    this.locatedServerUrl = null;
    this.baseResp = null;
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getLocatedServerName() {
    return this.locatedServerName;
  }

  public QueryMetaTableResponse setLocatedServerName(@org.apache.thrift.annotation.Nullable java.lang.String locatedServerName) {
    this.locatedServerName = locatedServerName;
    return this;
  }

  public void unsetLocatedServerName() {
    this.locatedServerName = null;
  }

  /** Returns true if field locatedServerName is set (has been assigned a value) and false otherwise */
  public boolean isSetLocatedServerName() {
    return this.locatedServerName != null;
  }

  public void setLocatedServerNameIsSet(boolean value) {
    if (!value) {
      this.locatedServerName = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.String getLocatedServerUrl() {
    return this.locatedServerUrl;
  }

  public QueryMetaTableResponse setLocatedServerUrl(@org.apache.thrift.annotation.Nullable java.lang.String locatedServerUrl) {
    this.locatedServerUrl = locatedServerUrl;
    return this;
  }

  public void unsetLocatedServerUrl() {
    this.locatedServerUrl = null;
  }

  /** Returns true if field locatedServerUrl is set (has been assigned a value) and false otherwise */
  public boolean isSetLocatedServerUrl() {
    return this.locatedServerUrl != null;
  }

  public void setLocatedServerUrlIsSet(boolean value) {
    if (!value) {
      this.locatedServerUrl = null;
    }
  }

  @org.apache.thrift.annotation.Nullable
  public BaseResp getBaseResp() {
    return this.baseResp;
  }

  public QueryMetaTableResponse setBaseResp(@org.apache.thrift.annotation.Nullable BaseResp baseResp) {
    this.baseResp = baseResp;
    return this;
  }

  public void unsetBaseResp() {
    this.baseResp = null;
  }

  /** Returns true if field baseResp is set (has been assigned a value) and false otherwise */
  public boolean isSetBaseResp() {
    return this.baseResp != null;
  }

  public void setBaseRespIsSet(boolean value) {
    if (!value) {
      this.baseResp = null;
    }
  }

  @Override
  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case LOCATED_SERVER_NAME:
      if (value == null) {
        unsetLocatedServerName();
      } else {
        setLocatedServerName((java.lang.String)value);
      }
      break;

    case LOCATED_SERVER_URL:
      if (value == null) {
        unsetLocatedServerUrl();
      } else {
        setLocatedServerUrl((java.lang.String)value);
      }
      break;

    case BASE_RESP:
      if (value == null) {
        unsetBaseResp();
      } else {
        setBaseResp((BaseResp)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case LOCATED_SERVER_NAME:
      return getLocatedServerName();

    case LOCATED_SERVER_URL:
      return getLocatedServerUrl();

    case BASE_RESP:
      return getBaseResp();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  @Override
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case LOCATED_SERVER_NAME:
      return isSetLocatedServerName();
    case LOCATED_SERVER_URL:
      return isSetLocatedServerUrl();
    case BASE_RESP:
      return isSetBaseResp();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that instanceof QueryMetaTableResponse)
      return this.equals((QueryMetaTableResponse)that);
    return false;
  }

  public boolean equals(QueryMetaTableResponse that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_locatedServerName = true && this.isSetLocatedServerName();
    boolean that_present_locatedServerName = true && that.isSetLocatedServerName();
    if (this_present_locatedServerName || that_present_locatedServerName) {
      if (!(this_present_locatedServerName && that_present_locatedServerName))
        return false;
      if (!this.locatedServerName.equals(that.locatedServerName))
        return false;
    }

    boolean this_present_locatedServerUrl = true && this.isSetLocatedServerUrl();
    boolean that_present_locatedServerUrl = true && that.isSetLocatedServerUrl();
    if (this_present_locatedServerUrl || that_present_locatedServerUrl) {
      if (!(this_present_locatedServerUrl && that_present_locatedServerUrl))
        return false;
      if (!this.locatedServerUrl.equals(that.locatedServerUrl))
        return false;
    }

    boolean this_present_baseResp = true && this.isSetBaseResp();
    boolean that_present_baseResp = true && that.isSetBaseResp();
    if (this_present_baseResp || that_present_baseResp) {
      if (!(this_present_baseResp && that_present_baseResp))
        return false;
      if (!this.baseResp.equals(that.baseResp))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetLocatedServerName()) ? 131071 : 524287);
    if (isSetLocatedServerName())
      hashCode = hashCode * 8191 + locatedServerName.hashCode();

    hashCode = hashCode * 8191 + ((isSetLocatedServerUrl()) ? 131071 : 524287);
    if (isSetLocatedServerUrl())
      hashCode = hashCode * 8191 + locatedServerUrl.hashCode();

    hashCode = hashCode * 8191 + ((isSetBaseResp()) ? 131071 : 524287);
    if (isSetBaseResp())
      hashCode = hashCode * 8191 + baseResp.hashCode();

    return hashCode;
  }

  @Override
  public int compareTo(QueryMetaTableResponse other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.compare(isSetLocatedServerName(), other.isSetLocatedServerName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLocatedServerName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.locatedServerName, other.locatedServerName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetLocatedServerUrl(), other.isSetLocatedServerUrl());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLocatedServerUrl()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.locatedServerUrl, other.locatedServerUrl);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = java.lang.Boolean.compare(isSetBaseResp(), other.isSetBaseResp());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetBaseResp()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.baseResp, other.baseResp);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  @Override
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  @Override
  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  @Override
  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("com.db.RPC.model.QueryMetaTableResponse(");
    boolean first = true;

    sb.append("locatedServerName:");
    if (this.locatedServerName == null) {
      sb.append("null");
    } else {
      sb.append(this.locatedServerName);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("locatedServerUrl:");
    if (this.locatedServerUrl == null) {
      sb.append("null");
    } else {
      sb.append(this.locatedServerUrl);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("baseResp:");
    if (this.baseResp == null) {
      sb.append("null");
    } else {
      sb.append(this.baseResp);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    if (locatedServerName == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'locatedServerName' was not present! Struct: " + toString());
    }
    if (locatedServerUrl == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'locatedServerUrl' was not present! Struct: " + toString());
    }
    if (baseResp == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'baseResp' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (baseResp != null) {
      baseResp.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class QueryMetaTableResponseStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public QueryMetaTableResponseStandardScheme getScheme() {
      return new QueryMetaTableResponseStandardScheme();
    }
  }

  private static class QueryMetaTableResponseStandardScheme extends org.apache.thrift.scheme.StandardScheme<QueryMetaTableResponse> {

    @Override
    public void read(org.apache.thrift.protocol.TProtocol iprot, QueryMetaTableResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LOCATED_SERVER_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.locatedServerName = iprot.readString();
              struct.setLocatedServerNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // LOCATED_SERVER_URL
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.locatedServerUrl = iprot.readString();
              struct.setLocatedServerUrlIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 255: // BASE_RESP
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.baseResp = new BaseResp();
              struct.baseResp.read(iprot);
              struct.setBaseRespIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    @Override
    public void write(org.apache.thrift.protocol.TProtocol oprot, QueryMetaTableResponse struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.locatedServerName != null) {
        oprot.writeFieldBegin(LOCATED_SERVER_NAME_FIELD_DESC);
        oprot.writeString(struct.locatedServerName);
        oprot.writeFieldEnd();
      }
      if (struct.locatedServerUrl != null) {
        oprot.writeFieldBegin(LOCATED_SERVER_URL_FIELD_DESC);
        oprot.writeString(struct.locatedServerUrl);
        oprot.writeFieldEnd();
      }
      if (struct.baseResp != null) {
        oprot.writeFieldBegin(BASE_RESP_FIELD_DESC);
        struct.baseResp.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class QueryMetaTableResponseTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    @Override
    public QueryMetaTableResponseTupleScheme getScheme() {
      return new QueryMetaTableResponseTupleScheme();
    }
  }

  private static class QueryMetaTableResponseTupleScheme extends org.apache.thrift.scheme.TupleScheme<QueryMetaTableResponse> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, QueryMetaTableResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      oprot.writeString(struct.locatedServerName);
      oprot.writeString(struct.locatedServerUrl);
      struct.baseResp.write(oprot);
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, QueryMetaTableResponse struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      struct.locatedServerName = iprot.readString();
      struct.setLocatedServerNameIsSet(true);
      struct.locatedServerUrl = iprot.readString();
      struct.setLocatedServerUrlIsSet(true);
      struct.baseResp = new BaseResp();
      struct.baseResp.read(iprot);
      struct.setBaseRespIsSet(true);
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
}
