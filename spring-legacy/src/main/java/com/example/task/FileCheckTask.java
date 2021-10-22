package com.example.task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.domain.AttachVO;
import com.example.service.AttachService;

@Component
public class FileCheckTask {

	private AttachService attachService;

	public FileCheckTask(AttachService attachService) {
		super();
		this.attachService = attachService;
	}

	// fixedDelay : 특정 시간 지나고 1번만
	// fixedRate : 스타트 시간을 시작으로 특정 시각 마다 반복함 (fixedRate = 5000) 5초마다 실행

	// spring 기능이지만 Quartz랑 연동되도록
	// 오전 2시에 작업 수행하기 // 초 분 시 일 월 요일 (년)
	@Scheduled(cron = "0 0 2 * * ?") // 0-5 0초에서 5초 까지, 0/5 0초부터 5초 마다
	public void checkFiles() {
		System.out.println("===========================================");
		System.out.println("checkFiles() task run ....");
		System.out.println("===========================================");

		// 어제날짜 구하기
		Calendar cal = Calendar.getInstance(); // 현재 날짜 시간 정보를 가진 calendar 객체 가져오기
		cal.add(Calendar.DATE, -1); // 현재 날짜시간에서 하루 빼기

		// 어제날짜 년월일 폴더경로 구하기
		Date yesterdayDate = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = sdf.format(yesterdayDate); // "2021/08/12"

		String path = "C:/jyh/upload/" + strDate; // C:/jyh/upload/2021/08/12

		// 어제날짜 경로를 파일객체로 준비
		File dir = new File(path);
		// 어제날짜 경로 폴더안에 있는 파일 목록을 배열로 가져오기
		File[] files = dir.listFiles();

		// 배열을 리스트컬렉션으로 변환하기
		List<File> fileList = Arrays.asList(files);

		// DB에서 년월일 경로에 해당하는 첨부파일 리스트 가져오기
		List<AttachVO> attachList = attachService.getAttachesByUploadpath(strDate);

		// ==============================================

		// 1) String요소를 가지는 List 두개로 contains() 포함여부로 비교하는 방법

		// fileList의 파일명만 가지는 filenameList 만들기
		List<String> filenameList = new ArrayList<>();
		for (File file : fileList) {
			filenameList.add(file.getName());
		}

		// attachList에서 파일명만 가지는 dbFilenameList 만들기
		List<String> dbFilenameList = new ArrayList<>();
		for (AttachVO attach : attachList) {
			dbFilenameList.add(attach.getUuid() + "_" + attach.getFilename());
			
			if(attach.getFiletype().equals("I")) {
				dbFilenameList.add("s_" + attach.getUuid() + "_" + attach.getFilename());
			}
		}

		// ==============================================

		for (String filename : filenameList) { // 실제 폴더에 있는 파일
			// 실제 파일이 DB에서 가져온 목록에 포함되어 있지 않으면
			if (dbFilenameList.contains(filename) == false) {
				File delFile = new File(path, filename);

				if (delFile.exists()) { // 해당 파일 존재하면
					delFile.delete();

					// 파일명 출력하기
					System.out.println(delFile.getName() + " 파일 삭제됨");
				}
			}
		} // for
	} // checkFiles

}
