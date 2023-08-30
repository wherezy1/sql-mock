package com.where.sqlmock.core.builder;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mifmif.common.regex.Generex;
import com.where.sqlmock.common.ErrorCode;
import com.where.sqlmock.core.generator.DataGenerator;
import com.where.sqlmock.core.generator.DataGeneratorFactory;
import com.where.sqlmock.core.utils.FakerUtils;
import com.where.sqlmock.core.model.enums.MockParamsRandomTypeEnum;
import com.where.sqlmock.core.model.enums.MockTypeEnum;
import com.where.sqlmock.core.schema.TableSchema;
import com.where.sqlmock.core.schema.TableSchema.Field;
import com.where.sqlmock.exception.BusinessException;
import com.where.sqlmock.model.entity.Dict;
import com.where.sqlmock.service.DictService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 数据生成器
 *
 */
public class DataBuilder {

    /**
     * 生成数据
     *
     * @param tableSchema
     * @param rowNum
     * @return
     */
    public static List<Map<String, Object>> generateData(TableSchema tableSchema, int rowNum) {
        List<Field> fieldList = tableSchema.getFieldList();
        // 初始化结果数据
        List<Map<String, Object>> resultList = new ArrayList<>(rowNum);
        for (int i = 0; i < rowNum; i++) {
            resultList.add(new HashMap<>());
        }
        // 依次生成每一列
        for (Field field : fieldList) {
            MockTypeEnum mockTypeEnum = Optional.ofNullable(MockTypeEnum.getEnumByValue(field.getMockType()))
                    .orElse(MockTypeEnum.NONE);
            DataGenerator dataGenerator = DataGeneratorFactory.getGenerator(mockTypeEnum);
            List<String> mockDataList = dataGenerator.doGenerate(field, rowNum);
            String fieldName = field.getFieldName();
            // 填充结果列表
            if (CollectionUtils.isNotEmpty(mockDataList)) {
                for (int i = 0; i < rowNum; i++) {
                    resultList.get(i).put(fieldName, mockDataList.get(i));
                }
            }
        }
        return resultList;
    }
}
