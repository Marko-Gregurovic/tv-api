package fer.hr.tvapi.repository;

import fer.hr.tvapi.entity.Channel;
import fer.hr.tvapi.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, Long> {

    Optional<Channel> findByName(String name);

    List<Channel> findByUser(Users user);

}
