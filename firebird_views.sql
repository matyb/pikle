CREATE VIEW ALL_TAB_COLUMNS(
    OWNER,
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    FB_DATA_TYPE,
    DATA_LENGTH,
    DATA_PRECISION,
    DATA_SCALE,
    NULLABLE,
    COLUMN_ID)
AS
SELECT
    r.RDB$OWNER_NAME,
    rf.RDB$RELATION_NAME,
    rf.RDB$FIELD_NAME,
    case f.RDB$FIELD_TYPE when 12 then 'DATE' when 14 then 'CHAR' when 16 then 'NUMBER' when 35 then 'DATE' when 37 then 'VARCHAR2' when 7 then 'NUMBER' when 8 then 'NUMBER' when 27 then 'NUMBER' when 261 then 'LONG' else f.RDB$FIELD_TYPE end,
    case f.RDB$FIELD_TYPE when 12 then 'DATE' when 14 then 'CHAR' when 16 then 'NUMERIC' when 35 then 'TIMESTAMP' when 37 then 'VARCHAR' when 7 then 'SMALLINT' when 8 then 'INTEGER' when 27 then 'DOUBLE' when 261 then 'BLOB' else f.RDB$FIELD_TYPE end,
    case f.RDB$FIELD_TYPE when 261 then f.RDB$SEGMENT_LENGTH else f.RDB$FIELD_LENGTH end ,
    0,
    0,
    case rf.rdb$null_flag when 1 then 'N' else 'Y' end,
    rf.RDB$FIELD_POSITION
FROM RDB$RELATIONS r
join RDB$RELATION_FIELDS rf on rf.RDB$RELATION_NAME=r.RDB$RELATION_NAME
left join RDB$FIELDS f on f.RDB$FIELD_NAME = rf.RDB$FIELD_SOURCE
where r.RDB$SYSTEM_FLAG=0
;

CREATE VIEW USER_IND_COLUMNS(
    INDEX_NAME,
    TABLE_OWNER,
    TABLE_NAME,
    COLUMN_NAME,
    COLUMN_POSITION)
AS
select
    s.RDB$INDEX_NAME,
    r.RDB$OWNER_NAME,
    i.RDB$RELATION_NAME,
    s.RDB$FIELD_NAME,
    s.RDB$FIELD_POSITION
from RDB$INDEX_SEGMENTS s,RDB$INDICES i,RDB$RELATIONS r
where s.RDB$INDEX_NAME  = i.RDB$INDEX_NAME
and i.RDB$RELATION_NAME = r.RDB$RELATION_NAME
and r.rdb$system_flag = 0
;

CREATE VIEW USER_TAB_COLUMNS(
    TABLE_NAME,
    COLUMN_NAME,
    DATA_TYPE,
    DATA_LENGTH,
    DATA_PRECISION,
    DATA_SCALE,
    NULLABLE,
    COLUMN_ID)
AS
SELECT
    rf.RDB$RELATION_NAME,
    rf.RDB$FIELD_NAME,
    case f.RDB$FIELD_TYPE when 12 then 'DATE' when 14 then 'CHAR' when 16 then 'NUMBER' when 35 then 'DATE' when 37 then 'VARCHAR2' when 7 then 'NUMBER' when 8 then 'NUMBER' when 27 then 'NUMBER' when 261 then 'LONG' else f.RDB$FIELD_TYPE end,
    case f.RDB$FIELD_TYPE when 261 then f.RDB$SEGMENT_LENGTH else f.RDB$FIELD_LENGTH end ,
    0,
    0,
    case rf.rdb$null_flag when 1 then 'N' else 'Y' end,
    rf.RDB$FIELD_POSITION
FROM RDB$RELATIONS r
join RDB$RELATION_FIELDS rf on rf.RDB$RELATION_NAME=r.RDB$RELATION_NAME
left join RDB$FIELDS f on f.RDB$FIELD_NAME = rf.RDB$FIELD_SOURCE
where r.RDB$SYSTEM_FLAG=0
;
