package com.example.demo;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
@RequestMapping("/board")
public class BoardController {
	final BoardDAO boardDAO;
	final MemberDAO memberDAO;
	final SessionManger sessionManger;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

//  미구현 
//	final BoardDAO board;

	@Autowired
	public BoardController(MemberDAO memberDAO, SessionManger sessionManger,BoardDAO boardDAO) {
		this.memberDAO = memberDAO;
		this.sessionManger = sessionManger;
		this.boardDAO = boardDAO;
	}

	@Value("${board.imgdir}")
	String fdir;
	
	
	//회원과 관련된 메인 메핑
	@GetMapping("/main")
		public String main1(HttpServletRequest request, Model m) {
		
		List<Board> list;
		// 세션 유지
		Member member = (Member) sessionManger.getSession(request);

		if (member == null) {
			m.addAttribute("member1", "nonmember");
		} else {
			m.addAttribute("member1", member);
		}
		
		return "index";
		
	}
	
	//메인 화면 페이지
	@GetMapping("/main/{page}")
	public String main(HttpServletRequest request, Model m,@PathVariable int page) {
		List<Board> list;
		// 세션 유지
		Member member = (Member) sessionManger.getSession(request);

		if (member == null) {
			m.addAttribute("member1", "nonmember");
		} else {
			m.addAttribute("member1", member);
		}
		
		try {
			//총 출력할 페이지 나올듯
			List<Integer> pages = pageFinder(page);
			list = boardDAO.getMainView(page);
			m.addAttribute("board","main");
			m.addAttribute("board1",list);
			m.addAttribute("page",pages);
			m.addAttribute("selectpage",page);
			
			logger.info("메인 화면 게시글 조회 성공");	
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("메인 화면 게시글 조회 실패");
		}
		return "index";
	}
	
	//선택한 게시글 보기
	@GetMapping("/view/{boardNumber}/{aid}")
	public String activeBoardNumber(HttpServletRequest request, Model m, 
			@PathVariable int boardNumber, @PathVariable int aid) {
	
		// 세션 유지
		Member member = (Member) sessionManger.getSession(request);

		System.out.println("member 객체 :" + member);

		if (member == null) {
			m.addAttribute("member1", "nonmember");
		} else {
			m.addAttribute("member1", member);
		}
		
		try {
			Board board = boardDAO.getBoard(boardNumber,aid);
			m.addAttribute("board","active");
			m.addAttribute("board1",board);
			logger.info("게시글 연결 성공");
			
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("게시글 연결 실패");
		}
		return "index";
	}

	/*회원 매핑*/
	@PostMapping("/member/addMember")
	public String addMember(@ModelAttribute Member member, Model m, HttpServletRequest request) {
		try {

//			System.out.println("암호 :"+ member.getPassword());
			if(memberDAO.addMember(member)) {
				logger.info("회원 가입 완료");
				request.setAttribute("newMember", "newmember");
				return "redirect:/board/main/1";
			}else {
				logger.info("ID 중복");
				
				m.addAttribute("IDerror1","IDerror2");
				
				return "index";
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("회원 가입시 문제 발생!!");
		}
		return "redirect:/board/main/1";
	}

	//회원 로그인
	@PostMapping("/member/login")
	public String login(@ModelAttribute Member member, Model m, HttpServletResponse response,HttpServletRequest request) {
		try {
			String info = memberDAO.login(member);

			if (info.equals("noneID")) {
				m.addAttribute("loginInfo", "fail");
				logger.info("로그인 실패");
				member = null;
				System.out.println("member 객체 : " + member);

				return main(request,m,1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		sessionManger.createSession(member, response);
		
		logger.info("로그인 성공");

		return "redirect:/board/main/1";
	}

	
	//회원 가입양식 페이지 이동
	@GetMapping("/member/signUp")
	public String signUp(Model m) {
		m.addAttribute("selectVeiwJsp", "signUp");
		return "index";
	}


	//로그 아웃시 세션 해제
	@GetMapping("/member/logout/{id}")
	public String logout(Model m, HttpServletRequest request, @PathVariable("id") String memberId) {
		
		try {
			memberDAO.log(memberId,"logout");
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("로그아웃 중 문제 발생");
		}
		sessionManger.expire(request);
		logger.info("로그아웃 성공");
		return "redirect:/board/main/1";
	}
	
	//회원 맴버 
	@GetMapping("/member/info/{id}")
	public String info(Model m,@PathVariable("id") String memberId,HttpServletRequest request) {
		try {
		
			Member member = memberDAO.memberSearch(memberId);
			
			m.addAttribute("member2",member);	
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.info("회원 정보 조회 중 문제 발생");
		}
		
		logger.info("회원 정보 조회 성공");
		
		m.addAttribute("selectVeiwJsp","memberPage");
		
		return main1(request,m);
	}
	
	//회원 수정 페이지 전달
	@GetMapping("/member/editMemberPage/{id}")
	public String editMember(Model m,@PathVariable("id") String memberId,HttpServletRequest request) {

		// 다시 회원가입 필요 필요
		logger.info("회원 수정 페이지 전달 완료");
		m.addAttribute("selectVeiwJsp", "editMember");
		
		return main1(request,m);
	}

	//회원 수정
	@PostMapping("/member/memberinfoChange")
	public String memberinfoChange(Model m, @ModelAttribute Member member,HttpServletRequest request) {
		try {
			memberDAO.memberUpdate(member);
			String str = "redirect:/board/member/info/"+member.getUserId();
			logger.info("회원 수정 성공");
			return str;
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("회원 수정 실패");
			String str = member.getUserId();
			return editMember(m,str,request);
		}
		
	}
	
	//회원 삭제
	@GetMapping("/member/removal/{id}")
	public String memberRemove(Model m, @PathVariable("id") String memberId,HttpServletRequest request) {
		try {
			memberDAO.memberDelete(memberId);
			sessionManger.expire(request);
			logger.info("회원 삭제 성공");
			return "redirect:/main";
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("회원 삭제 실패");
			return editMember(m,memberId,request);
		}
	}
	
	//해당 게시판 들어가기
	@GetMapping("/list/{boardnumber}/{page}")
	public String dramaList(HttpServletRequest request, Model m, @PathVariable int boardnumber, @PathVariable int page) {
		List<Board> list;
		// 세션 유지
		Member member = (Member) sessionManger.getSession(request);

		System.out.println("member 객체 :" + member);

		if (member == null) {
			m.addAttribute("member1", "nonmember");
		} else {
			m.addAttribute("member1", member);
		}
		
		try {
			
			List<Integer> pages = pageFinder2(page,boardnumber);
			list = boardDAO.getBoardList(boardnumber, page);
			m.addAttribute("board",Integer.toString(boardnumber));
			m.addAttribute("board1",list);
			m.addAttribute("page",pages);
			m.addAttribute("selectpage",page);
			logger.info("특정 게시글 조회 성공");
		
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("특정 게시글 조회 실패");
		}
		return "index";
	}
	
	//글쓰기 페이지 전달
	@GetMapping("/writeFormPage")
	public String writeFormPage(HttpServletRequest request,Model m) {
		m.addAttribute("write","write");
		logger.info("글 작성 페이지 전달");
	
		return main1(request,m);
	}
	
	//글수정 페이지 전달
	@GetMapping("/editFormPage/{aid}")
	public String editFormPage(@PathVariable int aid, HttpServletRequest request,Model m) {
		m.addAttribute("write","edit");
		m.addAttribute("aid",aid);
		logger.info("글 수정 페이지 전달");
		return main1(request,m);
	}
	
	//글작성
	@PostMapping("/write")
	public String write(HttpServletRequest request,Model m,@ModelAttribute Board board, @RequestParam("file") MultipartFile file) {
		
		try {
			Member member = (Member) sessionManger.getSession(request);
			
			
			//이미지 파일 현재 시각 더해서 서버에 저장
			if(!file.isEmpty()) {
				System.out.println("파일 이미지가 없다!!");
				Date now =new  Date();
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
				
				String fileName = format.format(now)+file.getOriginalFilename();
				
				File dest = new File(fdir+"/"+fileName);
				file.transferTo(dest);
				
				board.setImg(dest.getName());
			}else {
				board.setImg("");
			}
					
			boardDAO.write(board, member.getUserId());
			memberDAO.memberWrite(member);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("글 작성 실패");
		}
		

		logger.info("글 작성 완료 전달");
		return "redirect:/board/main/1";
	}
	
	//글 수정
	@PostMapping("/edit")
	public String edit(HttpServletRequest request,Model m,@ModelAttribute Board board, @RequestParam("file") MultipartFile file) {

		try {
			Member member = (Member) sessionManger.getSession(request);
			
			//이미지 파일 현재 시각 더해서 서버에 저장
			Date now =new  Date();
			
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
			
			String fileName = format.format(now)+file.getOriginalFilename();
			
			File dest = new File(fdir+"/"+fileName);
			file.transferTo(dest);
			
			board.setImg(dest.getName());
			
			boardDAO.edit(board, member.getUserId());
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("글 수정 실패");
		}
				
		logger.info("글 수정 완료");
		return "redirect:/board/main/list";
	}
	
	// 글 삭제
	@GetMapping("/delete/{aid}")
	public String delete(@PathVariable int aid, HttpServletRequest request,Model m) {
		
		try {
			Member member = (Member) sessionManger.getSession(request);
			boardDAO.delete(member.getUserId(),aid);
			logger.info("글 삭제 성공");
				return main(request,m,1);
			}catch(Exception e) {
				logger.info("글 삭제 성공");	
				return main(request,m,1);
			}

}
	//검색 기능
	@GetMapping("/list/args/{page}")
	public String searchTitleTotalBoard(Model m,HttpServletRequest request,@RequestParam(value = "title")String title,
			@PathVariable int page) {
		try {	
			
			Page pageList = new Page();
			int lastPage = boardDAO.searchTotalNumber(title)/15;
			
			//검색 총 페이지
			int realLastPage = (lastPage%15 == 0? lastPage :lastPage+1);
			
			pageList.setPageArray(page, realLastPage);
			
			
			List<Board> list;
			// 세션 유지
			Member member = (Member) sessionManger.getSession(request);
			if (member == null) {
				m.addAttribute("member1", "nonmember");
			} else {
				m.addAttribute("member1", member);
			}
			
			list = boardDAO.searchTotalList(title,page);
			m.addAttribute("board","main");
			m.addAttribute("board1",list);
			m.addAttribute("search","search");
			m.addAttribute("page",pageList.getPageArray());
			m.addAttribute("title",title);
			m.addAttribute("selectpage",page);
			logger.info("글 조회 성공");

		}catch(Exception e) {
			e.printStackTrace();
			logger.info("글 검색 실패");
		
		}
		return "index";
	}
	
	
	//특정 게시판에서 검색했을 때
	@GetMapping("/list/{boardNumber}/args/{page}")
	public String searchTitleBoard(Model m,HttpServletRequest request,@RequestParam(value = "title")String title,
			@PathVariable int boardNumber, @PathVariable int page)  {
		try {	
			List<Board> list;
			// 세션 유지
			Page pageList = new Page();
			
			int lastPage = boardDAO.searchBoardNumber(title,boardNumber)/15;
		
			
			//검색 총 페이지
			int realLastPage = (lastPage%15 == 0? lastPage :lastPage+1);
			
			if(realLastPage==0) {
				realLastPage =1 ;
			}
			
		
			
			pageList.setPageArray(page, realLastPage);
			
			Member member = (Member) sessionManger.getSession(request);
			
			if (member == null) {
				m.addAttribute("member1", "nonmember");
			} else {
				m.addAttribute("member1", member);
			}
			
			list = boardDAO.searchBoardList(title,boardNumber,page);
			m.addAttribute("board",Integer.toString(boardNumber));
			m.addAttribute("board1",list);
			m.addAttribute("search","search");
			m.addAttribute("page",pageList.getPageArray());
			m.addAttribute("title",title);
			m.addAttribute("selectpage",page);
			logger.info("글 조회 성공");
			
		}catch(Exception e) {
			e.printStackTrace();
			logger.info("글 검색 실패");
		
		}
		return "index";
	}
	
	//첫페이지, 마지막페이지 찾는 메소드
	public List<Integer> pageFinder(int presentPage){
		
		List<Integer> finder = new ArrayList<Integer>();	
	
		try {
			Page page = new Page();
			int maxRow = (int)(boardDAO.boardTotalNumber()/15);
			
			//마지막 페이지
			int lastPage = (maxRow == 0 ? maxRow:maxRow+1);	
			
			System.out.println("lastPage" + lastPage);
			
			System.out.println("presentPage "+presentPage);
			page = new Page();
			page.setPageArray(presentPage, lastPage);
			
			//화면에 출력될 페이지수 목록
			finder = page.getPageArray();
			
			logger.info("페이지 찾는거 성공");	
			
			return finder;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("페이즈 찾는거 실패");
			return null;
		}
	}
	
public List<Integer> pageFinder2(int presentPage,int number){
		
		List<Integer> finder = new ArrayList<Integer>();	
	
		try {
			Page page = new Page();
			int maxRow = (int)(boardDAO.boardTotalNumber(number)/15);
			
			//마지막 페이지
			int lastPage = (maxRow == 0 ? maxRow:maxRow+1);	
			
			System.out.println("lastPage" + lastPage);
			
			System.out.println("presentPage "+presentPage);
			page = new Page();
			page.setPageArray(presentPage, lastPage);
			
			//화면에 출력될 페이지수 목록
			finder = page.getPageArray();
			
			logger.info("페이지 찾는거 성공");	
			
			return finder;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("페이즈 찾는거 실패");
			return null;
		}
	}
}