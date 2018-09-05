var ioc = {
		config : {
			type : "org.nutz.ioc.impl.PropertiesProxy",
			fields : {
				paths : ["db.properties"]
			}
		},
		dataSource : {
			type : "com.alibaba.druid.pool.DruidDataSource",
			events : {
				depose : 'close'
			},
			fields : {
				driverClassName: {java :"$config.get('db_driver')"},
				url : {java :"$config.get('db_url')"},
				username : {java :"$config.get('db_name')"},
				password : {java :"$config.get('db_pass')"},
				filters:"stat"
//				testWhileIdle : true,
//				validationQuery : "select 1" ,
//				initialSize :  {java :"$config.get('initialSize')"},
//				maxActive : {java :"$config.get('maxActive')"},
//				maxWait: 15000,
			}
		},
		dao : {
			type : "org.nutz.dao.impl.NutDao",
			args : [{refer:"dataSource"}]
		}
};