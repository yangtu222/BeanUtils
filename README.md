# BeanUtils

This BeanUtils library is a Java bean copy utility with powerful functionality and high performance.

## Features:
* support copy with Java primitive type auto-convert to its Java type. e.g. int <=> Integer
* support copy with array type. e.g. int[] <=> Integer[]
* support copy with Java Collection type. e.g. List<FromBean> => List<ToBean>
* support copy with property name mapping. e.g. int id => int userId
* support copy with recursion copy. 
* support custom data convert when coping.
* with performance as native copy.
* easy usage, annotation way to define the property mapping.

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

## License

[Apache-2.0 License](LICENSE)

## Other

For Chinese user: http://www.jianshu.com/p/9a136ecd3838


