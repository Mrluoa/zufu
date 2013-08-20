package com.example.zhufu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.adapter.ListAdapter;
import com.adapter.ViewHolder;
import com.domain.Blessing;
import com.searchable.SearchableSave;
import com.tool.CommonURL;
import com.tool.HttpURL;
import com.tool.JSONTools;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
/**
 * 
 * @description ����UI
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-17 ����11:18:53
 */
public class SearchActivity extends Activity {
	private String query;
	private ListAdapter myAdapter = null;
	private ProgressDialog dialog;
	private ListView listview = null;
	private View view = null;
	private boolean isClick = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		// ��ʼ��
		myAdapter = new ListAdapter(this);

		listview = (ListView) findViewById(R.id.search_values);

		view = LayoutInflater.from(this).inflate(R.layout.search_footer, null);

		myAdapter.setList(new ArrayList<HashMap<String, Object>>());

		dialog = new ProgressDialog(SearchActivity.this);

		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("���ڲ�ѯף��...");
		dialog.setCancelable(true);

		// ����������
		onSearchRequested();
		//������������оͲ��ܵ���
		if (isClick) {
			// �����¼�
			listview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					ViewHolder viewHolder = (ViewHolder) view.getTag();
					Blessing bl = new Blessing();
					bl.setInfo(viewHolder.show_info.getText().toString());
					bl.setPick(viewHolder.show_pick.toString());
					bl.setSend(viewHolder.show_send.getText().toString());
					bl.setUrl(viewHolder.show_URL.toString());
					bl.setDate(viewHolder.show_date.getText().toString());
					bl.setFace(viewHolder.show_Face.toString());
					Intent intent = new Intent(SearchActivity.this,
							MiddleActivity.class);
					Bundle bu = new Bundle();
					System.out.println(bl.toString());
					bu.putSerializable("bless", bl);
					intent.putExtras(bu);
					startActivity(intent);
				}
			});
		}
	}

	@Override
	public boolean onSearchRequested() {
		// �򿪸��������򣨵�һ������Ĭ����ӵ��������ֵ��
		startSearch(null, false, null, false);
		return true;
	}

	// �õ��������
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// �����������ֵ
		query = intent.getStringExtra(SearchManager.QUERY);
		System.out.println("������==>" + query);
		if (!query.trim().equals("") && query.length() > 0) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("search", query);
			RunSearch(map);
		}
		// ����������¼
		SearchRecentSuggestions suggestions = new SearchRecentSuggestions(
				SearchActivity.this, SearchableSave.AUTHORITY,
				SearchableSave.MODE);
		suggestions.saveRecentQuery(query, null);
		System.out.println("����ɹ�");
	}

	public void RunSearch(Map<String, String> map) {
		new PostSearch().execute(map);
	}

	/**
	 * 
	 * @description �ύ��ѯ�����߳���
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-14 ����12:23:24
	 */
	private class PostSearch extends
			AsyncTask<Map<String, String>, Void, List<HashMap<String, Object>>> {

		@Override
		protected void onPreExecute() {
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected List<HashMap<String, Object>> doInBackground(
				Map<String, String>... params) {
			System.out.println("==========> ����ִ�� ");
			return JSONTools.getListData(HttpURL.PostBlessing(
					CommonURL.MALL_Search_URL, params[0]));
		}

		@Override
		protected void onPostExecute(List<HashMap<String, Object>> result) {
			// ������ݴ���0���ͼ��ص�listview��
			if (result.size() > 0) {
				isClick = true;
				listview.removeFooterView(view);
				myAdapter.setList(result);
				listview.setAdapter(myAdapter);
				dialog.dismiss();
			} else {
				isClick = false;
				myAdapter.setList(new ArrayList<HashMap<String, Object>>());
				listview.addFooterView(view);
				listview.setAdapter(myAdapter);
				dialog.dismiss();
			}
			super.onPostExecute(result);
		}

	}

}
