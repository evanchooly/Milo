package com.antwerkz.milo.grizzly;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;

public class GrizzlyServletOutputStream extends ServletOutputStream {
    private final OutputStream outputStream;

    public GrizzlyServletOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void write(int i) throws IOException {
        outputStream.write(i);
    }

    @Override
    public void write(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    @Override
    public void write(byte[] bytes, int i, int i1) throws IOException {
        outputStream.write(bytes, i, i1);
    }

    @Override
    public void flush() throws IOException {
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
        outputStream.close();
    }
}
