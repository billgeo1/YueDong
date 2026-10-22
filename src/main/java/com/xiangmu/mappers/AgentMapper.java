package com.xiangmu.mappers;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xiangmu.agent.AgentProductView;

@Mapper
public interface AgentMapper {
    List<AgentProductView> searchProducts(@Param("keyword") String keyword,
                                          @Param("categoryId") Integer categoryId,
                                          @Param("priceMin") Integer priceMin,
                                          @Param("priceMax") Integer priceMax,
                                          @Param("limit") int limit);

    AgentProductView findProductView(@Param("id") int id);

    Map<String, Object> businessOverview();

    List<Map<String, Object>> lowStockProducts(@Param("threshold") int threshold,
                                               @Param("limit") int limit);

    List<Map<String, Object>> salesRanking(@Param("limit") int limit);

    int shipOrderIfPending(@Param("id") int id);
}
