package io.blocko.api;

import io.blocko.dto.GroupUpdate;
import io.blocko.dto.UserInfo;
import io.blocko.response.ResultForm;
import io.blocko.service.GroupService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupApi {

  private final GroupService groupService;

  @GetMapping
  public ResponseEntity<ResultForm> findAll() {
    List<String> list = groupService.findAll();
    return ResponseEntity.ok(new ResultForm(list));
  }

  @GetMapping("/{group}")
  public ResponseEntity<ResultForm> findDetailByGroup(@PathVariable("group") String group) {
    List<UserInfo> userInfo = groupService.findDetailByGroup(group);
    return ResponseEntity.ok(new ResultForm(userInfo));
  }


  /**
   * 그룹 등록.
   * @param group
   * @return
   */
  @PostMapping("/{group}")
  public ResponseEntity<ResultForm> register(@PathVariable("group") String group) {
    return ResponseEntity.ok(new ResultForm(groupService.register(group)));
  }

  /**
   * 그룹 수정.
   * @param groupUpdate
   * @return
   */
  @PutMapping
  public ResponseEntity<ResultForm> update(@RequestBody GroupUpdate groupUpdate) {
    return ResponseEntity.ok(new ResultForm(groupService.update(groupUpdate)));
  }

  /**
   * 그룹 삭제.
   * @param group
   * @return
   */
  @DeleteMapping("/{group}")
  public ResponseEntity<ResultForm> delete(@PathVariable("group") String group) {
    return ResponseEntity.ok(new ResultForm(groupService.delete(group)));
  }
}
