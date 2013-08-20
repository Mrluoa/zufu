package com.example.zhufu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.ListAdapter;
import com.adapter.ViewHolder;
import com.domain.Blessing;
import com.tool.CommonURL;
import com.tool.DropDownListView;
import com.tool.DropDownListView.OnDropDownListener;
import com.tool.ExitApplication;
import com.tool.HttpURL;
import com.tool.JSONTools;

/**
 * 
 * @description ��ҳUI
 * @version 1.0
 * @author Mrluo
 * @date 2013-8-11 ����9:06:28
 */
public class HomeActivity extends Activity {
	private com.tool.DropDownListView listview;
	private RelativeLayout read_lodding = null;
	private int count = 1;

	private Button home_reset;

	private ListAdapter myadapter;

	private ImageView imageanmi = null;

	private int number = 12;// ÿ�λ�ȡ����������
	private int maxpage;// �ܹ��ж���ҳ
	private int row;// �ܹ����ݶ���
	// ȫ�ֵ���Դlist
	private List<HashMap<String, Object>> asylist = null;

	// ���ڼ�������
	private TextView textView1 = null;

	// ����ˢ��
	private boolean isDrow = false;
	// ��һ������
	private boolean isOneRun = true;
	private boolean loadfinish = false;
	// �ײ�������
	private boolean isScrol = false;

	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		// ��ӵ�ǰactivity
		ExitApplication.getInstance().addActivity(this);
		// ��ʼ��
		myadapter = new ListAdapter(this);
		listview = (DropDownListView) findViewById(R.id.home_listview);

		home_reset = (Button) findViewById(R.id.home_reset);
		read_lodding = (RelativeLayout) findViewById(R.id.read_lodding);
		imageanmi = (ImageView) findViewById(R.id.imageview);

		textView1 = (TextView) findViewById(R.id.textView1);

		// ��Activity�в��Ŷ���
		handler.postDelayed(new Runnable() {
			public void run() {
				AnimationDrawable frameAnimation = (AnimationDrawable) imageanmi
						.getDrawable();
				frameAnimation.start();
			}
		}, 50);

		// �ж�����
		isNetStart();

		// ˢ���¼�
		home_reset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ������ʾ�ж���
				read_lodding.setVisibility(View.VISIBLE);
				// ��ʾlistview
				listview.setVisibility(View.GONE);

				imageanmi.setVisibility(View.VISIBLE);
				textView1.setTextColor(Color.GRAY);
				textView1.setText("���ڼ�����....");

				// ��Activity�в��Ŷ���
				handler.postDelayed(new Runnable() {
					public void run() {
						AnimationDrawable frameAnimation = (AnimationDrawable) imageanmi
								.getDrawable();
						frameAnimation.start();
					}
				}, 50);

				isOneRun = true;
				isNetStart();
			}
		});
		// �����¼�
		listview.setOnDropDownListener(new OnDropDownListener() {

			@Override
			public void onDropDown() {
				System.out.println("�¼�-->����ˢ�³ɹ���");
				isDrow = true;
				new MallAsyncTask().execute(CommonURL.MALL_TJ_URL);
			}
		});

		// �ײ��¼�
		listview.setOnBottomListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int loadCount = myadapter.getCount();
				// if (isScrol && loadCount < row) {
				int page = row % myadapter.getCount();
				String sql = null;
				if (loadCount < row && count <= maxpage) {
					System.out.println("��ǰ:" + count + "����:" + maxpage);
					// ���ҳ�������ж�����

					if (count == maxpage) {
						System.out.println("���һҳ��Ŷ================= ִ�У�");
						sql = CommonURL.MALL_PAGE_URL + "&offone="
								+ (myadapter.getCount()) + "&offtwo=" + page;
					} else {
						sql = CommonURL.MALL_PAGE_URL + "&offone="
								+ (myadapter.getCount() + 1) + "&offtwo="
								+ number;
					}
					loadfinish = true;
					System.out.println("==================== " + sql);
					isScrol = true;
					new MallAsyncTask().execute(sql);
				} else {
					loadfinish = false;
					isScrol = false;
					listview.setHasMore(false);
					// ҪsetHashMaor��Ч����ִ��onBottomComplete
					listview.onBottomComplete();

				}
			}
		});
		// �����¼�
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				ViewHolder viewHolder = (ViewHolder) view.getTag();
				Blessing bl = new Blessing();
				bl.setInfo(viewHolder.show_info.getText().toString());
				bl.setPick(viewHolder.show_pick.toString());
				bl.setSend(viewHolder.show_send.getText().toString());
				bl.setUrl(viewHolder.show_URL.toString());
				bl.setDate(viewHolder.show_date.getText().toString());
				bl.setFace(viewHolder.show_Face.toString());
				Intent intent = new Intent(HomeActivity.this,
						MiddleActivity.class);
				Bundle bu = new Bundle();
				System.out.println(bl.toString());
				bu.putSerializable("bless", bl);
				intent.putExtras(bu);
				startActivity(intent);
			}
		});
	}

	/**
	 * 
	 * @description �첽����������
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-22 ����11:08:17
	 */
	private class MallAsyncTask extends
			AsyncTask<String, Void, List<HashMap<String, Object>>> {
		@Override
		protected List<HashMap<String, Object>> doInBackground(String... params) {

			String inputStream = HttpURL.getItentInputStram(params[0]);
			if (CommonURL.MALL_SJ_URL.equals(params[0])) {
				row = JSONTools.getListData(inputStream).size();
				System.out
						.println("=========================  count -> " + row);
				maxpage = row / number;
				System.out.println("===================  page " + maxpage);
			}
			return JSONTools.getListData(inputStream);
		}

		@Override
		protected void onPostExecute(List<HashMap<String, Object>> result) {
			// �����������0ִ��
			if (result.size() > 0) {
				System.out.println("��ɶ���");
				// ��һ�μ���
				if (isOneRun) {
					asylist = new ArrayList<HashMap<String, Object>>();
					asylist.addAll(result);
					// ���������е�����
					myadapter.setList(asylist);
					// ��������
					listview.setAdapter(myadapter);
					// ���ؼ����ж���
					read_lodding.setVisibility(View.GONE);
					// ��ʾlistview
					listview.setVisibility(View.VISIBLE);
					// �رն���
					AnimationDrawable frameAnimation = (AnimationDrawable) imageanmi
							.getDrawable();
					frameAnimation.stop();
					System.out.println("��һ��ִ��");
					// ���õ�һ�����н��Ϊfalse
					isOneRun = false;
					loadfinish = true;
				}
				// ������ˢ��Ϊ����ִ��
				if (isDrow) {
					// ��ʼ��listδ���µ�����,�����ײ����ظ�����
					asylist = new ArrayList<HashMap<String, Object>>();

					asylist.addAll(result);

					// ���������е�����
					myadapter.setList(asylist);
					// ˢ��������
					myadapter.notifyDataSetChanged();

					System.out.println("����ˢ��");
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"MM-dd HH:mm:ss");

					isDrow = false;
					loadfinish = false;
					count = 1;
					// ����û�м���Ϊû�оͰ���
					listview.setHasMore(true);
					// ҪsetHashMaor��Ч����ִ��onBottomComplete
					listview.onBottomComplete();

					// �رն���ˢ��
					listview.onDropDownComplete(dateFormat.format(new Date()));
					System.out.println("=======>�ر�ˢ�³ɹ���");
				}
				// ��ȡ�����������ݵ�����
				int loadCount = myadapter.getCount();
				// ���������ײ�ˢ�£���������С�ڵ�ǰ����������ֻ����һ��
				if (isScrol && loadCount < row && loadfinish) {
					loadfinish = false;
					System.out.println("�ײ�ˢ��");
					// �������
					asylist.addAll(result);
					// myadapter.setList(asylist);
					// ˢ��������
					myadapter.notifyDataSetChanged();
					System.out.println(myadapter.getCount());
					// �رյײ�����
					listview.onBottomComplete();
					// ��ǰҳ��
					count++;
				} else {
					listview.onBottomComplete();
				}

			}
			super.onPostExecute(result);
		}
	};

	/**
	 * 
	 * @description �ж������Ƿ����
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-7-24 ����3:56:47
	 * @param context
	 * @return
	 * @return boolean
	 * @throws
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		} else {
			listview.setEnabled(false);
			System.out.println("������");
		}
		return false;
	}

	/**
	 * ��Listview���ݷ���xml��
	 * 
	 * @description
	 * @version 1.0
	 * @author Mrluo
	 * @date 2013-8-11 ����9:09:57
	 * @return
	 * @return boolean
	 * @throws
	 */
	private void TextSaveXml() {
		List<HashMap<String, Object>> savelist = myadapter.getList();
		//ʵ����SharedPreferences���󣨵�һ����
		SharedPreferences mySharedPreferences = getSharedPreferences("zufu",
				Activity.MODE_PRIVATE);
		// ʵ����SharedPreferences.Editor���󣨵ڶ�����
		SharedPreferences.Editor editor = mySharedPreferences.edit();
		
		int count = savelist.size();
		for(int i = 0;i < count;i++){
			// ��putString�ķ�����������
			editor.putString("info", savelist.get(i).get("info").toString());
			editor.putString("send", savelist.get(i).get("send").toString());
			editor.putString("postdate", savelist.get(i).get("postdate").toString());
			editor.putString("pick", savelist.get(i).get("pick").toString());
			editor.putString("url", savelist.get(i).get("url").toString());
			editor.putString("face", savelist.get(i).get("face").toString());
		}
		// �ύ��ǰ����
		editor.commit();
		// ʹ��LogCat��Ϣ��ʾ����ʾ�ɹ�д������
		Log.i("����xml==>", "�����ݱ�����xml��");
	}

	/**
	 * ��������--�Ƿ��˳�����
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean flag = true;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			//TextSaveXml();
			// �Ƿ��˳�Ӧ��
			MainActivity.Exit(HomeActivity.this);
			
		}
		return flag;
	}

	private void isNetStart() {
		// �ػ�ӭ���淵�ص������ж������Ƿ�����
		if (isNetworkConnected(HomeActivity.this)) {

			new MallAsyncTask().execute(CommonURL.MALL_TJ_URL);
			new MallAsyncTask().execute(CommonURL.MALL_SJ_URL);

		} else {
			System.out.println("������״̬��");
			// ���ؼ����ж���
			// read_lodding.setVisibility(View.GONE);
			imageanmi.setVisibility(View.GONE);
			textView1.setText(Html.fromHtml("<u>������,�����������磡</u>"));
			textView1.setTextColor(Color.BLUE);
			textView1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					AlertDialog.Builder builder = new Builder(HomeActivity.this);
					builder.setTitle("��������");
					builder.setMessage("�����������������");
					builder.setPositiveButton("��������",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent();
									intent.setClassName("com.android.settings",
											"com.android.settings.WirelessSettings");
									startActivity(intent);
								}
							});
					builder.setNegativeButton("ȡ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub

								}
							});
					builder.create().show();
				}
			});
		}
	}
}
