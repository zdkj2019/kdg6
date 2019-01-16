package com.kdg6.definition;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListAdapter;
import android.widget.ListView;

public class DialogPopup  implements DialogInterface.OnClickListener {

	private AlertDialog mPopup;
	private ListAdapter mListAdapter;
	private CharSequence mPrompt;
	private Context mContext;
	private ListView listView;
	
	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	public void dismiss() {
		mPopup.dismiss();
		mPopup = null;
	}

	public boolean isShowing() {
		return mPopup != null ? mPopup.isShowing() : false;
	}

	public void setAdapter(ListAdapter adapter) {
		mListAdapter = adapter;
//		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		View itemView = inflater.inflate(R.layout.activity_dispatchinginformationreceiving, null);
//		listView = (ListView) itemView.findViewById(R.id.listView);
//		listView.setAdapter(adapter);
	}

	public void setPromptText(CharSequence hintText) {
		mPrompt = hintText;
	}

	public CharSequence getHintText() {
		return mPrompt;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		
	}

	public void show() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		if (mPrompt != null) {
			builder.setTitle(mPrompt);
		}
		mPopup = builder.setSingleChoiceItems(mListAdapter,-1, this).show();
	}

//	public void onClick(DialogInterface dialog, int which) {
//		setSelection(which);
//		// if (mOnItemClickListener != null) {
//		performItemClick(null, which, mListAdapter.getItemId(which));
//		// }
//		dismiss();
//	}
	


}