package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;

/** 
 * @Date 2018/4/16 10:18
 * @Author CycloneKid sk18810356@gmail.com 
 * @PackageName: cn.e3mall.search.service.impl
 * @ClassName: SearchItemServiceImpl 
 * @Description: solr的商品索引库管理
 *
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	/** 这里使用spring/applicationContext-solr.xml文件中配置的SolrServer对象 */
	@Autowired
	private SolrServer solrServer;

	/**
	 * @Date 2018/4/17 11:10
	 * @Author CycloneKid sk18810356@gmail.com
	 * @PackageName: cn.e3mall.search.service.impl
	 * @ClassName: SearchItemServiceImpl
	 * @Description:
	 *
	 */
	@Override
	public E3Result importAllItems() {
		try {
			/**从数据库中查出数据*/
			List<SearchItem> itemList = itemMapper.getItemList();

			/**遍历查出的数据，并将其添加到solr索引库中*/
			for (SearchItem searchItem : itemList) {

				SolrInputDocument document = new SolrInputDocument();

				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());

				solrServer.add(document);
			}
			solrServer.commit();
			return E3Result.ok();
		}catch (Exception e){
			e.printStackTrace();
			return E3Result.build(500, "商品导入失败");

		}
	}

	/**
	 * @Date 2018/6/8 21:41
	 * @Author CycloneKid sk18810356@gmail.com
	 * @MethodName: addDocument
	 * @Params: [itemId]
	 * @ReturnType: cn.e3mall.common.utils.E3Result
	 * @Description:
	 *
	 */
	public E3Result addDocument(long itemId) throws Exception {

		SearchItem searchItem = itemMapper.getItemById(itemId);
		SolrInputDocument document = new SolrInputDocument();

		document.addField("id", searchItem.getId());
		document.addField("item_title", searchItem.getTitle());
		document.addField("item_sell_point", searchItem.getSell_point());
		document.addField("item_price", searchItem.getPrice());
		document.addField("item_image", searchItem.getImage());
		document.addField("item_category_name", searchItem.getCategory_name());
//		document.addField("item_desc", searchItem.getItem_desc());

		solrServer.add(document);
		solrServer.commit();

		return E3Result.ok();
	}

}
