package co.assignment.searchapp.ui;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import co.assignment.searchapp.AppController;
import co.assignment.searchapp.R;
import co.assignment.searchapp.data.database.RecentSuggestionsProvider;
import co.assignment.searchapp.databinding.SearchActivityBinding;
import co.assignment.searchapp.ui.adapter.SearchListAdapter;
import co.assignment.searchapp.ui.viewmodel.SearchViewModel;

public class SearchActivity extends AppCompatActivity {
    private SearchListAdapter adapter;
    private SearchViewModel searchViewModel;
    private SearchActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        searchViewModel = new SearchViewModel(AppController.create(this));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new SearchListAdapter(getApplicationContext());

        searchViewModel.getArticleLiveData().observe(this, pagedList -> {
            adapter.submitList(pagedList);
        });

        searchViewModel.getNetworkState().observe(this, networkState -> {
            adapter.setNetworkState(networkState);
        });

        binding.searchView.setIconifiedByDefault(false);
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                loadSearchResults(query);
                binding.searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        binding.recyclerView.setAdapter(adapter);
    }

    public void loadSearchResults(String query) {
        if(query == null || query.trim().length() == 0) {
            adapter.submitList(null);
            return;
        }
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                RecentSuggestionsProvider.AUTHORITY, RecentSuggestionsProvider.MODE);
        suggestions.saveRecentQuery(query, null);
        searchViewModel.updateQueryText(query);
    }
}
