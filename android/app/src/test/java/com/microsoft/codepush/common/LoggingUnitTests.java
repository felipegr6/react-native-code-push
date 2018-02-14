package com.microsoft.codepush.common;

import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.codepush.common.datacontracts.CodePushDeploymentStatusReport;
import com.microsoft.codepush.common.datacontracts.CodePushDownloadStatusReport;
import com.microsoft.codepush.common.datacontracts.CodePushLocalPackage;
import com.microsoft.codepush.common.datacontracts.CodePushPackage;
import com.microsoft.codepush.common.datacontracts.CodePushUpdateRequest;
import com.microsoft.codepush.common.datacontracts.CodePushUpdateResponse;
import com.microsoft.codepush.common.exceptions.CodePushIllegalArgumentException;
import com.microsoft.codepush.common.utils.FileUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;

import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * This class tests cases where an error happens and should be logged via {@link AppCenterLog#error(String, String)}.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AppCenterLog.class)
public class LoggingUnitTests {

    private final static String CLIENT_UNIQUE_ID = "YHFfdsfdv65";
    private final static String DEPLOYMENT_KEY = "ABC123";
    private final static String LABEL = "awesome package";
    private final static boolean FAILED_INSTALL = false;
    private final static boolean IS_PENDING = true;
    private final static boolean IS_DEBUG_ONLY = false;
    private final static boolean IS_FIRST_RUN = false;
    private final static String APP_ENTRY_POINT = "/www/index.html";

    private FileUtils mFileUtils;

    @Before
    public void setUp() {
        this.mFileUtils = FileUtils.getInstance();
    }

    @Test(expected = CodePushIllegalArgumentException.class)
    public void testUpdateRequestAppVersionNull() throws Exception {
        CodePushPackage codePushPackage = new CodePushPackage();
        CodePushLocalPackage codePushLocalPackage = CodePushLocalPackage.createLocalPackage(FAILED_INSTALL, IS_FIRST_RUN, IS_PENDING, IS_DEBUG_ONLY, APP_ENTRY_POINT, codePushPackage);
        CodePushUpdateRequest codePushUpdateRequest = CodePushUpdateRequest.createUpdateRequest(DEPLOYMENT_KEY, codePushLocalPackage, CLIENT_UNIQUE_ID);
        codePushUpdateRequest.setAppVersion(null);
    }

    @Test(expected = CodePushIllegalArgumentException.class)
    public void testUpdateRequestDeploymentKeyNull() throws Exception {
        CodePushPackage codePushPackage = new CodePushPackage();
        CodePushLocalPackage codePushLocalPackage = CodePushLocalPackage.createLocalPackage(FAILED_INSTALL, IS_FIRST_RUN, IS_PENDING, IS_DEBUG_ONLY, APP_ENTRY_POINT, codePushPackage);
        CodePushUpdateRequest codePushUpdateRequest = CodePushUpdateRequest.createUpdateRequest(DEPLOYMENT_KEY, codePushLocalPackage, CLIENT_UNIQUE_ID);
        codePushUpdateRequest.setDeploymentKey(null);
    }

    @Test(expected = CodePushIllegalArgumentException.class)
    public void testDownloadReportLabelNull() throws Exception {
        CodePushDownloadStatusReport codePushDownloadStatusReport = CodePushDownloadStatusReport.createReport(CLIENT_UNIQUE_ID, DEPLOYMENT_KEY, LABEL);
        codePushDownloadStatusReport.setLabel(null);
    }

    @Test(expected = CodePushIllegalArgumentException.class)
    public void testDownloadReportClientIdNull() throws Exception {
        CodePushDownloadStatusReport codePushDownloadStatusReport = CodePushDownloadStatusReport.createReport(CLIENT_UNIQUE_ID, DEPLOYMENT_KEY, LABEL);
        codePushDownloadStatusReport.setClientUniqueId(null);
    }

    @Test(expected = CodePushIllegalArgumentException.class)
    public void testDownloadReportDeploymentKeyNull() throws Exception {
        CodePushDownloadStatusReport codePushDownloadStatusReport = CodePushDownloadStatusReport.createReport(CLIENT_UNIQUE_ID, DEPLOYMENT_KEY, LABEL);
        codePushDownloadStatusReport.setDeploymentKey(null);
    }

    @Test(expected = CodePushIllegalArgumentException.class)
    public void testDeploymentReportAppVersionNull() throws Exception {
        CodePushDeploymentStatusReport codePushDeploymentStatusReport = new CodePushDeploymentStatusReport();
        codePushDeploymentStatusReport.setAppVersion(null);
    }

    @Test(expected = CodePushIllegalArgumentException.class)
    public void testDeploymentReportPreviousKeyNull() throws Exception {
        CodePushDeploymentStatusReport codePushDeploymentStatusReport = new CodePushDeploymentStatusReport();
        codePushDeploymentStatusReport.setPreviousDeploymentKey(null);
    }

    @Test(expected = CodePushIllegalArgumentException.class)
    public void testUpdateResponseUpdateInfoNull() throws Exception {
        CodePushUpdateResponse codePushUpdateResponse = new CodePushUpdateResponse();
        codePushUpdateResponse.setUpdateInfo(null);
    }

    /**
     * Checks {@link FileUtils#finalizeResources} logs custom error message.
     */
    @Test
    public void testFinalizeResourcesLogging() {
        mockStatic(AppCenterLog.class);
        Closeable brokenResource = new Closeable() {
            @Override
            public void close() throws IOException {
                throw new IOException();
            }
        };
        mFileUtils.finalizeResources(Arrays.asList(brokenResource), "log me");
        verifyStatic(VerificationModeFactory.times(1));
    }
}