# BeanUtils

This BeanUtils library is a Java bean copy utility with powerful functionality and high performance.

## Features:
* support copy with Java primitive type auto-convert to its Java type. i.e: int <=> Integer
* support copy with array type. i.e: int[] <=> Integer[]
* support copy with Java Collection type. i.e: List<FromBean> => List<ToBean>
* support copy with property name mapping. i.e: int id => int userId
* support copy with recursion copy. 
* support custom data convert when coping.
* with performance as native copy.
* easy usage, annotation way to define the property mapping.

## Sample:
### Basic Copy
	FromBean fromBean = ...;
	ToBean toBean = BeanCopyUtils.copyBean(fromBean, ToBean.class);

### List Copy
	List<FromBean> fromList = ...;
	List<ToBean> toList = BeanCopyUtils.copyList(fromList, ToBean.class);
	
### Bean Copy Advanced
	BeanCopier copier = BeanCopyUtils.getBeanCopier(FromBean.class, ToBean.class);
	ToBean toBean = new ToBean();
	toBean = (ToBean)copier.copyBean(fromBean, toBean);

## Annotation configuration
### BeanPropertySource:
  	//use BeanPropertySource to specify the source class.
	@BeanPropertySource(source=FromBean.class)
	public class ToBean {
		...
	}
	
### BeanProperty: property name mapping.

	@BeanProperty(property="beanInt")
	private Integer toInt;
	
	@BeanProperty(property="bean2.beanInt")
	private int bean2Int;
	
	
	
