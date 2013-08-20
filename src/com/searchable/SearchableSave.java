package com.searchable;

import android.content.SearchRecentSuggestionsProvider;

public class SearchableSave extends SearchRecentSuggestionsProvider {

	public final static String AUTHORITY = "com.searchable.SearchableSave";
	public final static int MODE = DATABASE_MODE_QUERIES;

	public SearchableSave() {
		// TODO Auto-generated constructor stub
		super();
		setupSuggestions(AUTHORITY, MODE);
	}

}
