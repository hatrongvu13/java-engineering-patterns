package com.htv.patterns.behavioral.chain.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public final class OcrProcessingContext {

    private final String documentId;
    private final String fileName;
    private final OcrDocumentType documentType;
    private final byte[] content;

    private int width;
    private int height;
    private double qualityScore;
    private int orientationDegrees;

    private boolean noiseReduced;
    private boolean readyForOcr;

    private OcrProcessingStatus status;

    private final List<String> processingHistory;
    private final Map<String, Object> attributes;

    public OcrProcessingContext(
            String documentId,
            String fileName,
            OcrDocumentType documentType,
            byte[] content,
            int width,
            int height,
            double qualityScore,
            int orientationDegrees
    ) {
        this.documentId = requireText(documentId, "documentId");
        this.fileName = requireText(fileName, "fileName");
        this.documentType = Objects.requireNonNull(documentType, "documentType must not be null");
        Objects.requireNonNull(content, "content must not be null");
        this.content = Arrays.copyOf(content, content.length);
        this.width = width;
        this.height = height;
        this.qualityScore = qualityScore;
        this.orientationDegrees = normalizeOrientation(orientationDegrees);
        this.status = OcrProcessingStatus.RECEIVED;
        this.processingHistory = new ArrayList<>();
        this.attributes = new LinkedHashMap<>();
    }

    public String documentId() {
        return documentId;
    }

    public String fileName() {
        return fileName;
    }

    public OcrDocumentType documentType() {
        return documentType;
    }

    public byte[] content() {
        return Arrays.copyOf(content, content.length);
    }

    public int contentLength() {
        return content.length;
    }

    public int width() {
        return width;
    }

    public void setWidth(
            int width
    ) {
        this.width = width;
    }

    public int height() {
        return height;
    }

    public void setHeight(
            int height
    ) {
        this.height = height;
    }

    public double qualityScore() {
        return qualityScore;
    }

    public void setQualityScore(
            double qualityScore
    ) {
        this.qualityScore = qualityScore;
    }

    public int orientationDegrees() {
        return orientationDegrees;
    }

    public void setOrientationDegrees(int orientationDegrees) {
        this.orientationDegrees = normalizeOrientation(orientationDegrees);
    }

    public boolean noiseReduced() {
        return noiseReduced;
    }

    public void markNoiseReduced() {
        this.noiseReduced = true;
    }

    public boolean readyForOcr() {
        return readyForOcr;
    }

    public void markReadyForOcr() {
        this.readyForOcr = true;
        this.status = OcrProcessingStatus.READY;
    }

    public OcrProcessingStatus status() {
        return status;
    }

    public void setStatus(OcrProcessingStatus status) {
        this.status = Objects.requireNonNull(status, "status must not be null");
    }

    public void addHistory(String handlerCode) {
        processingHistory.add(requireText(handlerCode, "handlerCode"));
    }

    public List<String> processingHistory() {
        return Collections.unmodifiableList(processingHistory);
    }

    public void putAttribute(String key, Object value) {
        attributes.put(requireText(key, "key"), value);
    }

    public Object attribute(
            String key
    ) {
        return attributes.get(
                requireText(key, "key")
        );
    }

    public Map<String, Object> attributes() {
        return Collections.unmodifiableMap(
                attributes
        );
    }

    public String extension() {
        int separator = fileName.lastIndexOf('.');
        if (separator < 0 || separator == fileName.length() - 1) {
            return "";
        }
        return fileName.substring(separator + 1).toLowerCase(Locale.ROOT);
    }

    private static int normalizeOrientation(int degrees) {
        int normalized = degrees % 360;
        if (normalized < 0) {
            normalized += 360;
        }
        return normalized;
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        String normalized = value.trim();
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return normalized;
    }
}