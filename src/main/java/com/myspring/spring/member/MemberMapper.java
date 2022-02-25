package com.myspring.spring.member;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

@Mapper
public interface MemberMapper {
	// 멤버 등록
	@Insert("insert into membertable(id,password,name,tel,email,zipcode,addr1,addr2,authority) values (#{in.id},#{in.password},#{in.name},#{in.tel},#{in.email},#{in.zipcode},#{in.addr1},#{in.addr2},#{in.authority})")
	int insertMember(@Param("in") MemberVO member);

	// 전체 멤버 조회
//	@Select("select id, name, tel, email, zipcode, addr1, addr2, terms, point from membertable limit #{perPage} offset #{start}")
//	List<MemberVO> getMemberAll(@Param("start") int start, @Param("perPage") int perPage);

	// 전체 멤버 수 조회
	@Select("select count(id) from membertable")
	int getMemberCount();

	// 멤버 조회
	// select * from membertable where ? like %?%
	@SelectProvider(type = MemberUtils.class, method = "getMembers")
	List<MemberVO> getMembers(int start, int perPage, String condition, Object param);

	// 멤버 정보 수정
	// update membertable set ? = ?, ... where id = ?
	@UpdateProvider(type = MemberUtils.class, method = "updateMember")
	int updateMember(MemberVO member);

}
