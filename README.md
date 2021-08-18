# fjszf-journal-newspaper

报刊征订

### 初始化RDB TABLE

#### 报刊信息表

```sql
CREATE TABLE EGOV_JOURNAL_PAPER
(
    ID              CHAR(32)              NOT NULL,
    SORT_NO         INT     DEFAULT 0,              --排序号
    PUBLICATION     VARCHAR(256)          NOT NULL, --报刊名称
    POSTAL_DIS_CODE VARCHAR(100)          NOT NULL, --邮发代号
    JOURNAL         VARCHAR(32)           NOT NULL, --报纸/期刊
    LANG            VARCHAR(32)           NOT NULL, --语言，默认中文
    PAPER_TYPE      VARCHAR(100),                   --类型
    PERIODICAL      VARCHAR(32)           NOT NULL, --刊期：周刊、季刊、半年刊、月刊、年刊
    UNIT_PRICE      DECIMAL(10, 2)        NOT NULL, --价格
    YEAR_PRICE      DECIMAL(10, 2)        NOT NULL, --年价
    DELIVERY_METHOD VARCHAR(100)          NOT NULL, --订阅路径：默认 邮发
    BARCODE         VARCHAR(100),                   --条码号
    PRESS           VARCHAR(128),                   --出版社
    PHONE           VARCHAR(128),                   --联系电话
    PRESS_ADDRESS   VARCHAR(258),                   --出版社地址
    PROGRAMA        VARCHAR(128),                   --栏目
    PRESENTATION    VARCHAR(512),                   --介绍
    GOV_EXPENSE     boolean DEFAULT FALSE NOT NULL, --公费刊物
    IS_VALID        boolean DEFAULT TRUE  NOT NULL, --是否有效

    DRAFT_USER      VARCHAR(64),
    DRAFT_USER_NO   VARCHAR(16),
    DRAFT_ORG       VARCHAR(64),
    DRAFT_ORG_NO    VARCHAR(16),
    SYSTEM_NO       VARCHAR(64),
    CREATE_TIME     TIMESTAMP,
    UPDATE_TIME     TIMESTAMP,
    MANAGERS        CLOB,                           --管理员，群组或角色
    READERS         VARCHAR(64),                    --["*"] 所有人可见
    CONSTRAINT CONS1342192229 PRIMARY KEY (ID)
)
```

#### 报刊订阅表

```sql
-- DROP TABLE EGOV_NEWSPAPER_RSS;
CREATE TABLE EGOV_JOURNAL_SUBSCRIPTION
(
    ID                    CHAR(32)              NOT NULL,
    GOV_EXPENSE           BOOLEAN DEFAULT TRUE  NOT NULL, --订阅类型：自费，公费
    PUBLICATION           VARCHAR(256)          NOT NULL, --报刊名称
    POSTAL_DIS_CODE       VARCHAR(100)          NOT NULL, --邮发代号
    SUBSCRIBE_USER        VARCHAR(64),                    --订阅人
    SUBSCRIBE_USER_NO     VARCHAR(16),
    SUBSCRIBE_ORG         VARCHAR(64),                    --订阅处室
    SUBSCRIBE_ORG_NO      VARCHAR(16),
    SUBSCRIBE_YEAR        INT,                            --订阅年份
    SUBSCRIBE_MONTH_BEGIN INT     DEFAULT 1,              --起始月订期
    SUBSCRIBE_MONTH_END   INT     DEFAULT 12,             --截至月订期
    SUBSCRIBE_COPIES      INT,                            --订阅份数
    SUBSCRIBE_TIME        TIMESTAMP,                      --订阅时间
    CLEARING_FORM         VARCHAR(64),--结算方式：现金或赠送，默认现金

    IS_LEADER_PROVINCE    BOOLEAN DEFAULT FALSE NOT NULL, --是否省领导
    IS_LEADER_HALL        BOOLEAN DEFAULT FALSE NOT NULL, --是否厅领导
    CONSIGNEE             VARCHAR(64),                    --收件对象：处室收文、个人收件，不要下拉，默认处室收件

    VERIFY_STATUS         INT     DEFAULT 0     NOT NULL, --审核状态：0-草稿，1-待审核，2-已审核
    VERIFY_USER           VARCHAR(64),                    --审核人
    VERIFY_USER_NO        VARCHAR(16),
    VERIFY_TIME           TIMESTAMP,                      --审核时间

    DRAFT_USER            VARCHAR(64)           NOT NULL,
    DRAFT_USER_NO         VARCHAR(16)           NOT NULL,
    DRAFT_ORG             VARCHAR(64),
    DRAFT_ORG_NO          VARCHAR(16),
    SYSTEM_NO             VARCHAR(64),
    CREATE_TIME           TIMESTAMP,
    UPDATE_TIME           TIMESTAMP,
    MANAGERS              CLOB,                           --管理员，群组或角色
    CONSTRAINT CONS13421464262 PRIMARY KEY (ID)
)
```

### ACL角色及管理

#### 报刊管理员

    报刊管理员：`newspaper_manager`

#### 系统管理员

    系统管理员：`sys_manager`