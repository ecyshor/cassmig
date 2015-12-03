package com.ecyshor.cassmig.files;

import com.ecyshor.cassmig.model.LoadingConfig;

import java.io.InputStream;
import java.util.List;

public interface FileLoader<V extends LoadingConfig> {

	List<InputStream> loadFiles(V config);

}
