package com.witgather.utils;

import org.apache.commons.pool2.BaseKeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import com.witgather.baseadapter.BaseAdapter;
import com.witgather.service.user.LoginHandler;

public class ObjectPool {
	public static GenericKeyedObjectPool<String,BaseAdapter> objectpool = 
			new GenericKeyedObjectPool<String,BaseAdapter>(new BeanFactory());
	static {
		GenericKeyedObjectPoolConfig config = new GenericKeyedObjectPoolConfig();
		config.setMaxTotalPerKey(50);
		config.setMaxWaitMillis(10000);
		config.setMinIdlePerKey(5);
		objectpool.setConfig(config);
	}
}


class BeanFactory extends BaseKeyedPooledObjectFactory<String,BaseAdapter>{
	@Override
	public BaseAdapter create(String key) throws Exception {
		switch(key) {
		case "login":
			return new LoginHandler();
		case "register":
			return null;
			
		}
		
		return null;
	}

	@Override
	public PooledObject<BaseAdapter> wrap(BaseAdapter arg0) {
		return new DefaultPooledObject<BaseAdapter>(arg0);
	}


}