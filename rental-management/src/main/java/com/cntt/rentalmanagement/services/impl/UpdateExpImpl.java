//package com.cntt.rentalmanagement.services.impl;
//
//public class UpdateExpImpl {
//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    public void updateRoomStatus() {
//                String sql = "UPDATE room " +
//                "INNER JOIN contract ON room.id = contract.room_id " +
//                "SET room.status = 'QUA_HAN' " +
//                "WHERE contract.end_date < CURDATE()";
//        jdbcTemplate.update(sql);
//    }
//}
