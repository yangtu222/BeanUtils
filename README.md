# BeanUtils
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.yangtu222/BeanUtils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.yangtu222/BeanUtils)

This BeanUtils library is a Java bean copy utility with powerful functionality and high performance.

## Maven Usage
~~~
	<dependency>
		<groupId>com.github.yangtu222</groupId>
		<artifactId>BeanUtils</artifactId>
		<version>1.0.11</version>
	</dependency>
~~~

## Features:
* support copy with Java primitive type auto-convert to its Java type. e.g. int <=> Integer
* support copy with array type. e.g. int[] <=> Integer[]
* support copy with Java Collection type. e.g. List<BeanA> => List<BeanB>
* support copy with property name mapping. e.g. int id => int userId
* support copy with recursion copy. 
* support custom data convert when coping.
* with performance as native copy.
* easy usage, annotation way to define the property mapping.
* support one copy feature (IGNORE_PRIMITIVE_NULL_SOURCE_VALUE) (v1.0.2, thanks maosi)
* support copy with BeanA[] <==> List<BeanB> (version 1.0.2 )
* easy debug by dump the property mappings, and can be disabled using BeanCopyConfig. (version 1.0.2 )
* support copy with Java Enum <=> String (v1.0.4, thanks TuWei1992)
* support copy from JavaBean to String (v1.0.4, thanks TuWei1992, using Object.toString() )
* support one copy feature (IGNORE_ALL_NULL_SOURCE_VALUE) (v1.0.7, thanks sj853)

## A full Sample:

From class and To class:
~~~Java

	                                           @BeanCopySource(source=FromBean.class)          
	public class FromBean {                    public class ToBean {                           
	                                                                                       
		private boolean beanBool;              private Boolean beanBool;                   
		private byte beanByte;                 private Byte beanByte;                      
		private char beanChar;                 private Character beanChar;                 
		private short beanShort;               private Short beanShort;                    
		private int beanInt;                   private Integer beanInt;                    
		private Long beanLong;                 private long beanLong;                      
		private Float beanFloat;               private float beanFloat;                    
		private Double beanDouble;             private double beanDouble;                  
		private String beanString;             private String beanString;                  
		                                                                                   
		                                       @CopyPropery                                
		private MyEnum myEnum;                 private String myEnum;
		                                                                                   
		                                       @CopyPropery                                
		private String myEnum2;                private MyEnum myEnum2;
		                                                                                   
		                                       @CopyProperty(convertor=DateConvertor.class)
		private Date beanDate;                 private String beanDate;                    
		                                                                                   
		private int[] beanIntArray;            private int[] beanIntArray;                 
		                                                                                   
		                                       @CopyProperty                               
		private FromBean2 bean2;               private ToBean2 bean2;                      
		                                                                                   
		                                       @CopyCollection(targetClass=ToBean3.class)  
		private List<FromBean3> bean3List;     private List<ToBean3> bean3List;            
		                                                                                   
		                                       @CopyProperty                               
		private FromBean4[] bean4Array;        private ToBean4[] bean4Array;               
		                                                                                   
		// getters and setters...              @CopyProperty(property="beanInt")           
	}                                              private int beanId;                         
	                                                                                       
	                                               @CopyProperty(property="bean2.beanString")  
	                                               private String bean2String;                 
	                                           
	                                               // getters and setters...
	                                           }

~~~

And one line code as:

	ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);

## Performance:

|Library|1 time|100 times|10000 times|1000000 times|10000000 times|
|-|-|-|-|-|-|
|org.apache.commons.beanutils.BeanUtil.copyProperties|1|12|128|9963|99879|
|org.apache.commons.beanutils.PropertyUtils.copyProperties|0|2|56|5564|55651|
|org.springframework.beans.BeanUtils.copyProperties|0|2|5|473|4700|
|net.sf.ezmorph.bean.BeanMorpher|1|4|67|6769|68051|
|org.springframework.cglib.beans.BeanCopier.create 1|1|2|2|87|843|
|org.springframework.cglib.beans.BeanCopier.create 2|0|0|0|10|98|
|com.tuyang.beanutils.BeanCopyUtils.copyBean 1|0|0|0|21|196|
|com.tuyang.beanutils.BeanCopyUtils.copyBean 2|0|0|0|11|97|
|native Copy|0|0|0|10|88|

* The data in upper table is **millisecond**.
* The performance test is run on Win10, with intel i7 6700HQ, and Memory 24G.
* The test code is also contained in source code.
* 2 ways called in BeanCopier.create and BeanCopyUtils.copyBean also can be found in source code.
* native copy: that means using get/set methods called manually.

## API Usage:

~~~Java
	FromBean fromBean = ...;
	ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);
~~~

or:
~~~Java
	List<FromBean> fromList = ...;
	List<ToBean> toList = BeanCopyUtils.copyList(fromList, ToBean.class);
~~~

or:
~~~Java
	BeanCopier copier = BeanCopyUtils.getBeanCopier(FromBean.class, ToBean.class);
	ToBean toBean = new ToBean();
	toBean = (ToBean)copier.copyBean(fromBean, toBean);
~~~

or with option class, when ToBean class cannot be modified. At this time ToBeanOption class acts as a configuration class of ToBean class, and annotations are configured in this class:

~~~Java
	FromBean fromBean = ...;
	ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class, ToBeanOption.class);
	
	@BeanCopySource(source=FromBean.class)
	public class ToBeanOption {
		
		@BeanCopy(property="beanInt")
		private int beanId;
		...
		
~~~

## Annotation Detail
### BeanCopySource:

~~~Java
	//use @BeanCopySource to specify the source class.
	@BeanCopySource(source=FromBean.class)
	public class ToBean {
		...
	}
	
~~~

#### source
Specify the source object class.

#### features
Specify the copy features. The features are:
* IGNORE_PRIMITIVE_NULL_SOURCE_VALUE
  Ignore copying source null property to target primitive type property. e.g. Copy null (Integer type) to int.
  By default this feature is disabled, so we can debug where the pointer is null by thrown exception.

* IGNORE_ALL_NULL_SOURCE_VALUE
  Ignore copying source null property to target property.
  By default this feature is disabled.

* IGNORE_ENUM_CONVERT_EXCEPTION
  Ignore exceptions when converting from String to Enum when call Enum.valueOf(). This will happens when
  converting an invalid String value to Enum, and null will be set in this situation.

* ENABLE_JAVA_BEAN_TO_STRING
  Support converting a JavaBean to String, if the property is annotated with CopyProperty/CopyCollection.
  The implementation is using JavaBean's toString() method as the result, and null if the JavaBean is null.

### CopyProperty

#### default:
Used to specify the property should be copied to. e.g.

~~~Java
	@CopyProperty
	private ToBean2 bean2;
~~~

#### name mapping

Annotation CopyProperty's 'property' property is used for name mapping as following:

~~~Java
	@CopyProperty(property="beanInt")
	private Integer toInt;
	
	@CopyProperty(property="bean2.beanInt")
	private int bean2Int;

~~~

#### ignore
Annotation CopyProperty's 'ignored' property is used for ignoring the property when coping.
~~~Java
	@CopyProperty(ignored=true)
	private Integer beanInt;
~~~
#### convertor
Annotation CopyProperty's 'convertor' property is used for custom data converting.
~~~Java
	@CopyProperty(convertor=DateConvertor.class)
	private String beanDate;
~~~

The convertor class must be BeanCopyConvertor's implementation class and have a default constructor.
Here is a sample: 

~~~Java
public class GendorConvertor implements BeanCopyConvertor<Integer, String> {

	@Override
	public String convertTo(Integer object) {
		if( object == 1 ) 
			return "Male";
		if( object == 2) 
			return "Female";
		return "Unknown";
	}
}
~~~

#### optionClass
Annotation CopyProperty's 'optionClass' property is used for specifying the option class when do recursion copy.
~~~Java
	@CopyProperty(optionClass=ToBeanOption.class)
	private ToBean bean2;
~~~
### CopyCollection

#### targetClass

Due to the limitation of Java, it is hard to get the class type of Collection's template parameter. So the targetClass should be specified as the Collection's template class.
~~~Java
	@CopyCollection(targetClass=ToBean2.class)
	private List<ToBean2> beanList;
~~~
#### name mapping/ignore/optionClass
name mapping/ignore/optionClass is the same to CopyProperty.

## Advance

* Disable Javassist library: if your project is not enable to use Javassist library, e.g. Android project, you can change BeanCopyConfig's beanCopyFactory to ReflactBeanCopyFactory.class, which is using Java reflect to archive the bean copy.

* In collection copy, if the property is an abstract collection class, the default implementation class will be:
    - List: ArrayList
    - Set : HashSet
    - Deque: ArrayDeque
    - Queue: ArrayDeque

If you want to use other class as default implementation class, you can change BeanCopyConfig's related properties.

* ClassLoader (1.0.6): You can specify a classLoader instance to BeanCopyConfig, otherwise BeanUtils will use toBean.getClass().getClassLoader() as the classLoader to avoid multi-classLoader issue.
  

## License

[Apache-2.0 License](LICENSE)

## Other

For Chinese user: http://www.jianshu.com/p/9a136ecd3838


