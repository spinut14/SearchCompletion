package com.fwk.es;

import java.io.IOException;

public interface CallESService {

	public String sendToEs(String jsonStr) throws IOException, Exception;
}
