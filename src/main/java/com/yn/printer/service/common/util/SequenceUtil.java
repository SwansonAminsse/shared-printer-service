package com.yn.printer.service.common.util;

import com.google.common.base.Strings;
import com.yn.printer.service.modules.meta.entity.MetaSequence;
import com.yn.printer.service.modules.meta.repository.MetaSequenceRepository;
import org.apache.commons.lang3.StringUtils;

/**
 * 序列工具类
 * 提供一些辅助静态方法处理自定义序列
 *
 * @author huabiao
 * @create 2023/12/08  15:42
 */
public final class SequenceUtil {

    private SequenceUtil() {
    }

    /**
     * 自定义序列-查询
     *
     * @param name                   序列名称
     * @param metaSequenceRepository
     * @return MetaSequence
     * @author huabiao
     * @create 2023/12/11  10:08
     */
    private static MetaSequence find(String name, MetaSequenceRepository metaSequenceRepository) {
        final MetaSequence sequence = metaSequenceRepository.findByName(name);
        if (sequence == null) {
            throw new IllegalArgumentException("No such sequence: " + name);
        }
        return sequence;
    }

    /**
     * 自定义序列-获取给定序列的下一个序列值
     *
     * @param name 序列名称
     * @return 下一个序列值
     * @description
     * @author huabiao
     * @create 2023/12/11  10:09
     */
    public static String nextValue(String name) {
        final MetaSequenceRepository metaSequenceRepository = SpringContextHolder.getBean(MetaSequenceRepository.class);
        final MetaSequence sequence = find(name, metaSequenceRepository);
        final Long next = sequence.getNext();
        final String prefix = sequence.getPrefix();
        final String suffix = sequence.getSuffix();
        final Integer padding = sequence.getPadding();

        String value = "" + next;
        if (padding > 0) {
            value = Strings.padStart(value, padding, '0');
        }
        if (!StringUtils.isBlank(prefix)) {
            value = prefix + value;
        }
        if (!StringUtils.isBlank(suffix)) {
            value = value + suffix;
        }
        sequence.setNext(next + sequence.getIncrement());
        metaSequenceRepository.save(sequence);
        return value;
    }


    /**
     * 自定义序列-设置给定序列的下一个数值
     * <p>这个方法必须在一个正在运行的事务中调用，因为会更新MetaSequence数据库</p>
     *
     * @param name 序列名称
     * @param next 下一个序列号
     * @author huabiao
     * @create 2023/12/11  10:10
     */
    public static void nextValue(final String name, final long next) {
        final MetaSequenceRepository metaSequenceRepository = SpringContextHolder.getBean(MetaSequenceRepository.class);
        final MetaSequence sequence = find(name, metaSequenceRepository);
        sequence.setNext(next);
        metaSequenceRepository.save(sequence);
    }
}
