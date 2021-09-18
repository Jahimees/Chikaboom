package net.chikaboom.dao;

import net.chikaboom.constant.TableConstant;
import net.chikaboom.model.*;

public enum TableEnum {

    ACCOUNT {{
        setEntity(new Account());
        setTableName(TableConstant.ACCOUNT);
    }},
    CLIENT {{
        setEntity(new Client());
        setTableName(TableConstant.CLIENT);
    }},
    COMMENT {{
        setEntity(new Comment());
        setTableName(TableConstant.COMMENT);
    }},
    MASTER {{
        setEntity(new Master());
        setTableName(TableConstant.MASTER);
    }},
    SERVICE {{
        setEntity(new Service());
        setTableName(TableConstant.SERVICE);
    }},
    SERVICE_TYPE {{
        setEntity(new ServiceType());
        setTableName(TableConstant.SERVICE_TYPE);
    }},
    WORK {{
        setEntity(new Work());
        setTableName(TableConstant.WORK);
    }};

    private Entity entity;
    private String tableName;

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
